import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Comparator;

public class CatalogService {
   private final BST<PCPart> skuIndex;
   private final BST<PCPart> secondaryIndex;

   private final Comparator<PCPart> skuComparator;
   private final Comparator<PCPart> secondaryComparator;

   public CatalogService() {
      this.skuComparator = new SkuComparator();
      this.secondaryComparator = new NameCategoryComparator();
      this.skuIndex = new BST<>();
      this.secondaryIndex = new BST<>();
   }

   public Comparator<PCPart> getSkuComparator() {
      return skuComparator;
   }

   public Comparator<PCPart> getSecondaryComparator() {
      return secondaryComparator;
   }

   public void addProduct(PCPart part) {
      if (part == null) {
         throw new IllegalArgumentException("part cannot be null");
      }

      if (searchByPrimaryKey(part.getSku()) != null) {
         throw new IllegalArgumentException("duplicate SKU: " + part.getSku());
      }

      skuIndex.insert(part, skuComparator);
      secondaryIndex.insert(part, secondaryComparator);
   }

   public PCPart removeProduct(String sku) {
      PCPart existing = searchByPrimaryKey(sku);
      if (existing == null) {
         return null;
      }

      skuIndex.remove(existing, skuComparator);
      secondaryIndex.remove(existing, secondaryComparator);
      return existing;
   }

   public PCPart searchByPrimaryKey(String sku) {
      if (sku == null) {
         return null;
      }
      return skuIndex.search(PCPart.keyBySku(sku), skuComparator);
   }

   public LinkedList<PCPart> searchBySecondaryKey(String nameKey, String category) {
      LinkedList<PCPart> matches = new LinkedList<>();
      if (nameKey == null || nameKey.trim().isEmpty()) {
         return matches;
      }

      secondaryIndex.inOrderForEach(part -> {
         boolean sameName = part.getNameKey().equalsIgnoreCase(nameKey);
         boolean sameCategory = category == null || category.trim().isEmpty() || part.getCategory().equalsIgnoreCase(category);
         if (sameName && sameCategory) {
            matches.addLast(part);
         }
      });
      return matches;
   }

   public String displaySortedByPrimaryKey() {
      return skuIndex.inOrderString();
   }

   public String displaySortedBySecondaryKey() {
      return secondaryIndex.inOrderString();
   }

   public LinkedList<PCPart> searchByPriceRange(double minPrice, double maxPrice) {
      LinkedList<PCPart> matches = new LinkedList<>();
      skuIndex.inOrderForEach(part -> {
         if (part.getPrice() >= minPrice && part.getPrice() <= maxPrice) {
            matches.addLast(part);
         }
      });
      return matches;
   }

   public boolean updatePrice(String sku, double newPrice) {
      if (newPrice < 0) {
         return false;
      }

      PCPart part = searchByPrimaryKey(sku);
      if (part == null) {
         return false;
      }

      part.setPrice(newPrice);
      return true;
   }

   public boolean updateSpecs(String sku, String newSpecs) {
      PCPart part = searchByPrimaryKey(sku);
      if (part == null) {
         return false;
      }

      part.setSpecs(newSpecs);
      return true;
   }

   public boolean updateStock(String sku, int newStock) {
      if (newStock < 0) {
         return false;
      }

      PCPart part = searchByPrimaryKey(sku);
      if (part == null) {
         return false;
      }

      int currentStock = part.getInStock();
      if (newStock > currentStock) {
         part.addToStock(newStock - currentStock);
      } else if (newStock < currentStock) {
         part.removeFromStock(currentStock - newStock);
      }
      return true;
   }

   /**
    * Loads parts from a CSV file with columns:
    * SKU(or SK), NameKey, Category, Price, InStock, Specs.
    * Supports quoted fields that may contain commas.
    * @param csvPath path to CSV file
    * @return number of successfully added products
    * @throws IOException when file access fails
    */
   public int loadFromCsv(Path csvPath) throws IOException {
      int loaded = 0;
      try (BufferedReader br = Files.newBufferedReader(csvPath)) {
         String line;
         boolean isFirstLine = true;

         while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
               continue;
            }

            ArrayList<String> fields = parseCsvLine(line);
            if (isFirstLine && looksLikeHeader(fields)) {
               isFirstLine = false;
               continue;
            }
            isFirstLine = false;

            if (fields.size() < 6) {
               continue;
            }

            String sku = cleanField(fields.get(0));
            String nameKey = cleanField(fields.get(1));
            String category = cleanField(fields.get(2));
            double price = parsePrice(fields.get(3));
            int inStock = Integer.parseInt(cleanField(fields.get(4)));
            String specs = cleanField(fields.get(5));

            addProduct(new PCPart(sku, nameKey, category, price, inStock, specs));
            loaded++;
         }
      }
      return loaded;
   }

   /** Writes current catalog contents to a CSV file (sorted by SKU). */
   public void writeToCsv(Path csvPath) throws IOException {
      try (BufferedWriter bw = Files.newBufferedWriter(csvPath)) {
         bw.write("SKU,NameKey,Category,Price,InStock,Specs");
         bw.newLine();
         skuIndex.inOrderForEach(part -> {
            try {
               bw.write(csvEscape(part.getSku()));
               bw.write(",");
               bw.write(csvEscape(part.getNameKey()));
               bw.write(",");
               bw.write(csvEscape(part.getCategory()));
               bw.write(",");
               bw.write(Double.toString(part.getPrice()));
               bw.write(",");
               bw.write(Integer.toString(part.getInStock()));
               bw.write(",");
               bw.write(csvEscape(part.getSpecs()));
               bw.newLine();
            } catch (IOException e) {
               throw new RuntimeException(e);
            }
         });
      } catch (RuntimeException re) {
         if (re.getCause() instanceof IOException) {
            throw (IOException) re.getCause();
         }
         throw re;
      }
   }

   private static String csvEscape(String value) {
      if (value == null) return "";
      boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
      String v = value.replace("\"", "\"\"");
      return needsQuotes ? "\"" + v + "\"" : v;
   }

   private static boolean looksLikeHeader(ArrayList<String> fields) {
      if (fields.size() < 3) {
         return false;
      }
      String first = cleanField(fields.get(0)).toLowerCase();
      String second = cleanField(fields.get(1)).toLowerCase();
      return (first.equals("sku") || first.equals("sk")) && second.contains("name");
   }

   private static String cleanField(String field) {
      String value = field == null ? "" : field.trim();
      if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
         value = value.substring(1, value.length() - 1);
      }
      return value;
   }

   private static double parsePrice(String rawPrice) {
      String cleaned = cleanField(rawPrice).replace("$", "").replace(",", "");
      return Double.parseDouble(cleaned);
   }

   private static ArrayList<String> parseCsvLine(String line) {
      ArrayList<String> fields = new ArrayList<>();
      StringBuilder current = new StringBuilder();
      boolean inQuotes = false;

      for (int i = 0; i < line.length(); i++) {
         char ch = line.charAt(i);
         if (ch == '"') {
            if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
               current.append('"');
               i++;
            } else {
               inQuotes = !inQuotes;
            }
         } else if (ch == ',' && !inQuotes) {
            fields.add(current.toString());
            current.setLength(0);
         } else {
            current.append(ch);
         }
      }
      fields.add(current.toString());
      return fields;
   }

   private static class SkuComparator implements Comparator<PCPart> {
      @Override
      public int compare(PCPart a, PCPart b) {
         return a.getSku().compareTo(b.getSku());
      }
   }

   private static class NameCategoryComparator implements Comparator<PCPart> {
      @Override
      public int compare(PCPart a, PCPart b) {
         int nameCompare = a.getNameKey().compareToIgnoreCase(b.getNameKey());
         if (nameCompare != 0) {
            return nameCompare;
         }

         int categoryCompare = a.getCategory().compareToIgnoreCase(b.getCategory());
         if (categoryCompare != 0) {
            return categoryCompare;
         }

         return a.getSku().compareTo(b.getSku());
      }
   }
}
