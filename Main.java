/** Main.java
 * @author Felipa Mendez
 * @author Peilian Song
 */
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException {
        HashTable<Customer> customers = new HashTable<>(10);
        Map<String, Customer> customerByUsername = new HashMap<>();

        HashTable<Employee> employees = new HashTable<>(10);
        CatalogService catalog = new CatalogService();
        try {
            int loaded = catalog.loadFromCsv(Paths.get("pc_parts_products.csv"));
            System.out.println("Loaded " + loaded + " products into catalog.");
        } catch (IOException e) {
            System.out.println("Warning: could not load product catalog: " + e.getMessage());
        }
        Comparator<Order> orderByPriority = (a, b) -> Integer.compare(a.getPriorityScore(), b.getPriorityScore());
        Heap<Order> orderHeap = new Heap<>(new ArrayList<>(), orderByPriority);


        ///////////// USER LOGIN PRINT OUTS ////////////////
        //dummy users for testing
        Customer dummyCustomer = new Customer("Alicia", "Smith", "alicia@email.com", "1234", 
            "123 Main St", "San Jose", "CA", "95112");
        customers.add(dummyCustomer);
        customerByUsername.put(dummyCustomer.getUsername(), dummyCustomer);

        employees.add(new Employee("Bobby", "Jones", "bobby@email.com", "abcd", true));

        //seeding users_upload.txt file
        Scanner file = new Scanner(new File("users_upload.txt"));

        //USER SCAN
        while(file.hasNext()) {
            String first = file.next();

            // stop if we reach employees section
            if (first.equals("EMPLOYEES")) {
                break;
            }
            // skip section header so we don't use "CUSTOMERS" as a first name
            if (first.equals("CUSTOMERS")) {
                continue;
            }

            String last = file.next();
            String username = file.next();
            String password = file.next();
            String address = file.next();
            String city = file.next();
            String state = file.next();
            String zip = file.next();

            Customer c = new Customer(first, last, username, password, address, city, state, zip);

            customers.add(c);
            customerByUsername.put(username, c);
        } 

        //EMPLOYEE SCAN
        while (file.hasNext()) {
            String first = file.next();

            // stop if we reach orders
            if (first.equals("ORDERS")) {
                break;
            }
            // skip section header so we don't use "EMPLOYEES" as first name
            if (first.equals("EMPLOYEES")) {
                continue;
            }

            String last = file.next();
            String email = file.next();
            String password = file.next();
            boolean manager = file.nextBoolean();

            Employee e = new Employee(first, last, email, password, manager);

            employees.add(e);
        }

        //ORDER SCAN
        while (file.hasNext()) {

            int orderId = file.nextInt();
            String username = file.next();
            int shippingSpeed = file.nextInt();

            LinkedList<PCPart> items = new LinkedList<>();

            // read product SKUs until end of line
            String line = file.nextLine().trim();

            if (!line.isEmpty()) {
                Scanner skuScan = new Scanner(line);

                while (skuScan.hasNext()) {

                    String sku = skuScan.next();
                    PCPart part = catalog.searchByPrimaryKey(sku); // is this right?

                    items.addLast(part);
                }

                skuScan.close();
            }

            Customer customer = customerByUsername.get(username);

            Order order = new Order(orderId, customer, items, shippingSpeed);

            orderHeap.insert(order); // add to priority queue 
        }

        file.close(); // end file scan start new scaner

        // Next ID for new orders (optional: set from max loaded order ID + 1)
        int[] nextOrderId = new int[1];
        nextOrderId[0] = 10000;

        Scanner input = new Scanner(System.in);

        int choice = 0;

        while (choice != 4) { 
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Continue as Guest");
            System.out.println("4. Quit");
            System.out.print("Enter choice: ");

            
            choice = input.nextInt();
            input.nextLine();

            //lets use 3 differend methods to separate the employee, guest, and customer menu options
            //print out files one for customer.txt, employee.txt, manager.txt or something

            if (choice == 1) { 
                login(customers, employees, catalog, orderHeap, nextOrderId, input);
            } else if (choice == 2) { //create an account 
                createAccount(customers, input);
            } else if (choice == 3) { //guest menu options
                System.out.println("\nLogged in as Guest.");
                guestInterface();
                
            } else if (choice == 4) {
                System.out.println("Exiting program...");

            } else {
                System.out.println("Invalid choice. Try again.");
            }

        }

        input.close();

    }

    /** login
     * 
     */
    public static void login(HashTable<Customer> customers, HashTable<Employee> employees,
                            CatalogService catalog, Heap<Order> orderHeap, int[] nextOrderId, Scanner input) {

        System.out.print("Enter username: ");
        String username = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        Customer tempCustomer = new Customer("", "", username, password, "", "", "", "");
        Employee tempEmployee = new Employee("", "", username, password, false);

        Customer foundCustomer = customers.get(tempCustomer);
        Employee foundEmployee = employees.get(tempEmployee);

        if (foundCustomer != null && foundCustomer.passwordMatch(password)) {
            System.out.println("Customer login successful!");
            System.out.println(foundCustomer);
            customerInterface(foundCustomer, catalog, orderHeap, nextOrderId, input);
        } else if (foundEmployee != null && foundEmployee.passwordMatch(password)) {
            System.out.println("Employee login successful!");
            System.out.println(foundEmployee);
            if (foundEmployee.getIsManager()) {
                managerInterface(catalog, orderHeap, input);
            } else {
                employeeInterface(catalog, orderHeap, input);
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    /** createAccount
     * 
     */
    public static void createAccount(HashTable<Customer> customerTable, Scanner input) {

        System.out.print("Enter Username: ");
        String username = input.nextLine();

        System.out.print("Please Enter Your Password: ");
        String password = input.nextLine();

        System.out.print("Please Enter Your Firstname: ");
        String firstname = input.nextLine();

        System.out.print("Please Enter Your Lastname: ");
        String lastname = input.nextLine();

        Customer searchKey = new Customer(firstname, lastname, username, password, "", "", "", "");

        int bucketIndex = customerTable.find(searchKey);
        if (bucketIndex != -1) {
            System.out.println("User already exists! Please login with " + username + ".");
            return;
        }

        System.out.print("Please Enter Your Address: ");
        String address = input.nextLine();
        System.out.print("Please Enter Your City: ");
        String city = input.nextLine();
        System.out.print("Please Enter Your State: ");
        String state = input.nextLine();
        System.out.print("Please Enter Your ZIP: ");
        String zip = input.nextLine();

        Customer newCustomer = new Customer(firstname, lastname, username, password, 
            address, city, state, zip);
        customerTable.add(newCustomer);

        System.out.println("Account created successfully! You can now login.");
    }

    /** customerInterface
     * 
     */
    public static void customerInterface(
        Customer currentCustomer,
        CatalogService catalog,
        Heap<Order> orderHeap,
        int[] nextOrderId,
        Scanner input
    ) {
        int choice = -1;

        while (choice != 9) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. Search for a product");
            System.out.println("2. Find product by primary key");
            System.out.println("3. Find product by secondary key");
            System.out.println("4. List all products (primary key order)");
            System.out.println("5. List all products (secondary key order)");
            System.out.println("6. Place an order");
            System.out.println("7. View purchases (all orders)");
            System.out.println("8. View shipped / unshipped orders");
            System.out.println("9. Quit and write to file(s)");
            System.out.print("Enter choice: ");

            choice = input.nextInt();
            input.nextLine();

            switch (choice){
                case 1:
                    //searchForProduct(catalog, input);
                    break;
                case 2:
                    System.out.print("Enter SKU: ");
                    String searchSku = input.nextLine().trim();
                    PCPart found = catalog.searchByPrimaryKey(searchSku);
                    if (found != null) System.out.println(found);
                    else System.out.println("No product found with SKU: " + searchSku);
                    break;
                case 3:
                    System.out.print("Enter product name: ");
                    String searchName = input.nextLine().trim();
                    System.out.print("Enter category (or press Enter for any): ");
                    String searchCat = input.nextLine().trim();
                    LinkedList<PCPart> searchResults = catalog.searchBySecondaryKey(searchName, searchCat);
                    if (searchResults.getLength() == 0) System.out.println("No products found.");
                    else System.out.println(searchResults);
                    break;
                case 4:
                    //listAllProductsByPrimaryKey(catalog, input);
                    break;
                case 5:
                    //listAllProductsBySecondaryKey(catalog, input);
                    break;
                case 6:
                    placeOrder(currentCustomer, catalog, orderHeap, nextOrderId, input);
                    break;
                case 7:
                    //viewPurchases(currentCustomer, input);
                    break;
                case 8:
                    //viewShippedUnshippedOrders(currentCustomer, input);
                    break;
                case 9:

                    System.out.println("Saving and returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        // - Search for a product
            // - Find and display one record using the primary key (BST search - updated to return the value instead of a boolean)
            // - Find and display one record using the secondary key (BST search - updated to return the value instead of a boolean)

        // - List Database of Products
            // - List data sorted by primary key (BST display)
            // - List data sorted by secondary key (BST display)

        // - Place an Order (add a new Order to the heap)
            // - Overnight Shipping
            // - Rush Shipping
            // - Standard Shipping

        // - View Purchases
            // - View shipped orders (LinkedList display)
            // - View unshipped orders (LinkedList display)
            // - Quit and Write to file(s) (update file(s) to reflect customer and order info changes) 
    }

    /** Place an order: collect cart by SKU, shipping speed, then add to heap and customer unshipped. */
    public static void placeOrder(
        Customer currentCustomer,
        CatalogService catalog,
        Heap<Order> orderHeap,
        int[] nextOrderId,
        Scanner input
    ) {
        LinkedList<PCPart> cart = new LinkedList<>();
        System.out.print("Enter SKU (Enter when done): ");
        for (String sku = input.nextLine().trim(); !sku.isEmpty(); sku = input.nextLine().trim()) {
            PCPart part = catalog.searchByPrimaryKey(sku);
            if (part != null) cart.addLast(part);
            else System.out.println("SKU not found: " + sku);
            System.out.print("Next SKU (Enter when done): ");
        }
        if (cart.getLength() == 0) {
            System.out.println("Cart empty. Order cancelled.");
            return;
        }
        System.out.print("Shipping 1=Standard 2=Rush 3=Overnight: ");
        int shippingSpeed = Math.max(1, Math.min(3, input.nextInt()));
        input.nextLine();

        int orderId = nextOrderId[0]++;
        Order newOrder = new Order(orderId, currentCustomer, cart, shippingSpeed);
        orderHeap.insert(newOrder);
        currentCustomer.addOrder(newOrder);
        System.out.println("Order placed. ID: " + orderId);
    }

    /** guestInterface
     * 
     */
    public static void guestInterface() {
        
        
    }

    /** Ship the top-priority order: remove from heap, mark shipped, move on customer's lists. */
    public static void shipTopOrder(Heap<Order> orderHeap) {
        if (orderHeap.isEmpty()) {
            System.out.println("No orders to ship.");
            return;
        }
        Order toShip = orderHeap.extractMax();
        toShip.setShipped(true);

        Customer customer = toShip.getCustomer();
        if (customer != null) {
            customer.markOrderShipped(toShip.getOrderId());
        } else {
            System.out.println("Warning: shipped order " + toShip.getOrderId()
                               + " has no customer attached (heap only).");
        }

        System.out.println("Shipped: " + toShip);
    }

    /** employeeInterface
     *
     */
    public static void employeeInterface(CatalogService catalog, Heap<Order> orderHeap, Scanner input) {
        int choice = -1;
        while (choice != 6) {
            System.out.println("\n=== Employee Menu ===");
            System.out.println("1. Search for an order (by order id)");
            System.out.println("2. Search for an order (by customer first and last name)");
            System.out.println("3. View order with highest priority");
            System.out.println("4. View all orders sorted by priority");
            System.out.println("5. Ship an order");
            System.out.println("6. Quit and write to file(s)");
            System.out.print("Enter choice: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter order ID: ");
                    int searchId = input.nextInt();
                    input.nextLine();
                    boolean foundById = false;
                    for (int i = 1; i <= orderHeap.getHeapSize(); i++) {
                        Order o = orderHeap.getElement(i);
                        if (o.getOrderId() == searchId) {
                            System.out.println(o);
                            foundById = true;
                            break;
                        }
                    }
                    if (!foundById) System.out.println("No order found with ID: " + searchId);
                    break;
                case 2:
                    System.out.print("Enter customer first name: ");
                    String searchFirst = input.nextLine().trim();
                    System.out.print("Enter customer last name: ");
                    String searchLast = input.nextLine().trim();
                    boolean foundByName = false;
                    for (int i = 1; i <= orderHeap.getHeapSize(); i++) {
                        Order o = orderHeap.getElement(i);
                        Customer c = o.getCustomer();
                        if (c != null
                                && c.getFirstName().equalsIgnoreCase(searchFirst)
                                && c.getLastName().equalsIgnoreCase(searchLast)) {
                            System.out.println(o);
                            foundByName = true;
                        }
                    }
                    if (!foundByName) System.out.println("No orders found for: " + searchFirst + " " + searchLast);
                    break;
                case 3:
                    if (orderHeap.isEmpty()) System.out.println("No orders in queue.");
                    else System.out.println("Highest priority order:\n" + orderHeap.getMax());
                    break;
                case 4:
                    if (orderHeap.isEmpty()) {
                        System.out.println("No orders in queue.");
                    } else {
                        ArrayList<Order> sorted = orderHeap.heapSortSnapshot();
                        System.out.println("\n=== All Orders by Priority (Highest to Lowest) ===");
                        for (int i = 0; i < sorted.size(); i++) {
                            System.out.println((i + 1) + ". " + sorted.get(i));
                        }
                    }
                    break;
                case 5:
                    shipTopOrder(orderHeap);
                    break;
                case 6:
                    System.out.println("Saving and returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }


    /** managerInterface
     * 
     */
    public static void managerInterface(CatalogService catalog, Heap<Order> orderHeap, Scanner input) {
        int choice = -1;
        while (choice != 6) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1. Search for an order (by order id)");
            System.out.println("2. Search for an order (by customer first and last name)");
            System.out.println("3. View order with highest priority");
            System.out.println("4. View all orders sorted by priority");
            System.out.println("5. Ship an order");
            System.out.println("6. Quit and write to file(s)");
            System.out.println("7. Add new product");
            System.out.println("8. Update existing product");
            System.out.println("9. Remove product");
            System.out.print("Enter choice: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    // searchByOrderId(...);
                    break;
                case 2:
                    // searchByCustomerName(...);
                    break;
                case 3:
                    // viewHighestPriorityOrder(orderHeap);
                    break;
                case 4:
                    // viewAllOrdersSortedByPriority(orderHeap);
                    break;
                case 5:
                    shipTopOrder(orderHeap);
                    break;
                case 6:
                    System.out.println("Saving and returning to main menu...");
                    break;
                case 7:
                    // addProduct(catalog, input);
                    break;
                case 8:
                    // updateProduct(catalog, input);
                    break;
                case 9:
                    // removeProduct(catalog, input);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    
    



}