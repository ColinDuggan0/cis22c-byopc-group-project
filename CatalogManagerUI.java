import java.util.Scanner;

/**
 * UI helper methods for Manager product-catalogue updates (BST by primary key / SKU).
 */
public class CatalogManagerUI {

    /**
     * Top-level manager option: Update Products Catalogue By Primary Key.
     * Provides sub-actions: add, update, remove.
     */
    public static void updateProductsCatalogueByPrimaryKey(CatalogService catalog, Scanner input) {
        int productChoice = -1;
        while (productChoice != 4) {
            System.out.println("\nUpdate Products Catalogue By Primary Key:");
            System.out.println("1. Add New Product (BST insert)");
            System.out.println("2. Update Existing Product (price/description/stock)");
            System.out.println("3. Remove a Product (BST remove)");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            productChoice = input.nextInt();
            input.nextLine();

            switch (productChoice) {
                case 1:
                    System.out.print("Enter SKU: ");
                    String newSku = input.nextLine().trim();
                    System.out.print("Enter name/key: ");
                    String newName = input.nextLine().trim();
                    System.out.print("Enter category: ");
                    String newCategory = input.nextLine().trim();
                    System.out.print("Enter price: ");
                    double newPrice = input.nextDouble();
                    input.nextLine();
                    System.out.print("Enter initial stock: ");
                    int newStock = input.nextInt();
                    input.nextLine();
                    System.out.print("Enter specs/description: ");
                    String newSpecs = input.nextLine().trim();

                    try {
                        catalog.addProduct(new PCPart(newSku, newName, newCategory, newPrice, newStock, newSpecs));
                        System.out.println("Product added: " + newSku);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Enter SKU to update: ");
                    String updateSku = input.nextLine().trim();
                    PCPart toUpdate = catalog.searchByPrimaryKey(updateSku);

                    if (toUpdate == null) {
                        System.out.println("Product not found: " + updateSku);
                        break;
                    }

                    System.out.println("Found: " + toUpdate);
                    System.out.println("  1. Update price");
                    System.out.println("  2. Update specs/description");
                    System.out.println("  3. Add more stock");
                    System.out.print("Enter choice: ");

                    int updateChoice = input.nextInt();
                    input.nextLine();

                    switch (updateChoice) {
                        case 1:
                            System.out.print("Enter new price: ");
                            double newPrice2 = input.nextDouble();
                            input.nextLine();
                            catalog.updatePrice(updateSku, newPrice2);
                            System.out.println("Price updated.");
                            break;

                        case 2:
                            System.out.print("Enter new specs/description: ");
                            String newSpecs2 = input.nextLine().trim();
                            catalog.updateSpecs(updateSku, newSpecs2);
                            System.out.println("Specs/description updated.");
                            break;

                        case 3:
                            System.out.print("Enter amount to add to stock: ");
                            int addAmount = input.nextInt();
                            input.nextLine();
                            toUpdate.addToStock(addAmount);
                            System.out.println("Stock updated. New stock: " + toUpdate.getInStock());
                            break;

                        default:
                            System.out.println("Invalid choice.");
                    }
                    break;

                case 3:
                    System.out.print("Enter SKU to remove: ");
                    String removeSku = input.nextLine().trim();
                    PCPart removed = catalog.removeProduct(removeSku);
                    if (removed != null) System.out.println("Removed: " + removed);
                    else System.out.println("Product not found: " + removeSku);
                    break;

                case 4:
                    // Back to manager menu.
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

