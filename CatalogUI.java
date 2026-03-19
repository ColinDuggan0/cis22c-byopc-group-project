import java.util.Scanner;

/** UI helper methods for catalog-related customer menus. */
public class CatalogUI {

    /** Top-level search menu: choose primary-key or secondary-key search. */
    public static void searchForProduct(CatalogService catalog, Scanner input) {
        System.out.println("\nSearch for a product:");
        System.out.println("1. Find by primary key");
        System.out.println("2. Find by secondary key");
        System.out.print("Enter choice: ");
        int searchChoice = input.nextInt();
        input.nextLine();

        switch (searchChoice) {
            case 1:
                findProductByPrimaryKey(catalog, input);
                break;
            case 2:
                findProductBySecondaryKey(catalog, input);
                break;
            default:
                System.out.println("Invalid search choice.");
        }
    }

    /** Top-level listing menu: choose primary-key or secondary-key display. */
    public static void listDatabaseOfProducts(CatalogService catalog, Scanner input) {
        System.out.println("\nList database of products:");
        System.out.println("1. List sorted by primary key");
        System.out.println("2. List sorted by secondary key");
        System.out.print("Enter choice: ");
        int listChoice = input.nextInt();
        input.nextLine();

        switch (listChoice) {
            case 1:
                listAllProductsByPrimaryKey(catalog);
                break;
            case 2:
                listAllProductsBySecondaryKey(catalog);
                break;
            default:
                System.out.println("Invalid list choice.");
        }
    }

    /** Find and display one product by primary key (SKU) using the catalog's primary-key BST. */
    private static void findProductByPrimaryKey(CatalogService catalog, Scanner input) {
        System.out.print("Enter product SKU (primary key): ");
        String sku = input.nextLine().trim();

        if (sku.isEmpty()) {
            System.out.println("SKU cannot be empty.");
            return;
        }

        PCPart part = catalog.searchByPrimaryKey(sku);
        if (part == null) {
            System.out.println("No product found with SKU: " + sku);
        } else {
            System.out.println("Product found:");
            System.out.println(part);
        }
    }

    /** Find and display product(s) by secondary key (name key, optional category) using the catalog's secondary-key BST. */
    private static void findProductBySecondaryKey(CatalogService catalog, Scanner input) {
        System.out.print("Enter product name key (secondary key): ");
        String nameKey = input.nextLine().trim();

        if (nameKey.isEmpty()) {
            System.out.println("Name key cannot be empty.");
            return;
        }

        System.out.print("Enter category (or press Enter for any): ");
        String category = input.nextLine().trim();
        if (category.isEmpty()) {
            category = null;
        }

        LinkedList<PCPart> matches = catalog.searchBySecondaryKey(nameKey, category);
        if (matches == null || matches.getLength() == 0) {
            System.out.println("No products found matching that name/category.");
            return;
        }

        System.out.println("Matching products:");
        matches.positionIterator();
        while (!matches.offEnd()) {
            System.out.println(matches.getIterator());
            matches.advanceIterator();
        }
    }

    /** List all products sorted by primary key using the primary-key BST display. */
    private static void listAllProductsByPrimaryKey(CatalogService catalog) {
        System.out.println("\nAll products (sorted by primary key / SKU):");
        System.out.println(catalog.displaySortedByPrimaryKey());
    }

    /** List all products sorted by secondary key using the secondary-key BST display. */
    private static void listAllProductsBySecondaryKey(CatalogService catalog) {
        System.out.println("\nAll products (sorted by secondary key / name+category):");
        System.out.println(catalog.displaySortedBySecondaryKey());
    }
}

