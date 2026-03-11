/*
 * PCPart.java
 * CIS 22C Project
 * Author: Colin Duggan
 */

public class PCPart {
   private String sku;
   private String nameKey;
   private String category;
   private double price;
   private int inStock;
   private String specs;

   /* Creates an “empty” part (mostly used if you need it for testing). */
   public PCPart() {
      sku = "";
      nameKey = "";
      category = "";
      price = 0.0;
      inStock = 0;
      specs = "";
   }

   /* Creates a fully-populated part (normal constructor for loading from file). */
   public PCPart(String sku, String nameKey, String category, double price, int inStock, String specs) {
      this.sku = sku;
      this.nameKey = nameKey;
      this.category = category;
      this.price = price;
      this.inStock = inStock;
      this.specs = specs;
   }

   /* Creates a key-only object used to search the SKU BST. */
   public static PCPart keyBySku(String sku) {
      return new PCPart(sku, "", "", 0.0, 0, "");
   }

   /* Creates a key-only object used to search the NameKey BST. */
   public static PCPart keyByNameKey(String nameKey) {
      return new PCPart("", nameKey, "", 0.0, 0, "");
   }

   /* Getters. */
   public String getSku() {
      return sku;
   }

   public String getNameKey() {
      return nameKey;
   }

   public String getCategory() {
      return category;
   }

   public double getPrice() {
      return price;
   }

   public int getInStock() {
      return inStock;
   }

   public String getSpecs() {
      return specs;
   }

   /* Setters for fields you’ll actually update in manager/customer flows. */
   public void setPrice(double price) {
      this.price = price;
   }

   public void setSpecs(String specs) {
      this.specs = specs;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   /* Adds stock (manager option). */
   public void addToStock(int amount) {
      if (amount > 0) {
         inStock += amount;
      }
   }

   /* Removes stock if possible (order placement). Returns true if it worked. */
   public boolean removeFromStock(int amount) {
      if (amount <= 0) {
         return false;
      }
      if (inStock < amount) {
         return false;
      }
      inStock -= amount;
      return true;
   }

   /* Prints nicely in menus / listings. */
   public String toString() {
      return sku + " | " + category + " | " + nameKey + " | $" + String.format("%.2f", price)
            + " | Stock: " + inStock + " | " + specs;
   }

   /* Equality based on SKU (handy if you ever compare parts directly). */
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null || !(obj instanceof PCPart)) {
         return false;
      }
      PCPart other = (PCPart) obj;

      if (sku == null || other.sku == null) {
         return false;
      }
      return sku.equals(other.sku);
   }
}