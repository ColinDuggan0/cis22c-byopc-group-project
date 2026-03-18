/** Main.java
 * @author Felipa Mendez
 * @author Peilian Song
 */
import java.util.Comparator;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HashTable<Customer> customers = new HashTable<>(10);
        HashTable<Employee> employees = new HashTable<>(10);
        CatalogService catalog = new CatalogService();
        catalog.loadFromXlsx(java.nio.file.Path.of("pc_parts_products.xlsx"));
        Heap<Order> orderHeap = new Heap<>(new ArrayList<Order>(), getOrderComparator());

        // dummy users for testing
        customers.add(new Customer("Alicia", "Smith", "alicia@email.com", "1234",
            "123 Main St", "San Jose", "CA", "95112"));
        employees.add(new Employee("Bobby", "Jones", "bobby@email.com", "abcd", true));

        // seeding users_upload.txt file
        Scanner file = new Scanner(new File("users_upload.txt"));

        // USER SCAN
        while (file.hasNext()) {
            String first = file.next();

            // stop if we reach employees section
            if (first.equals("EMPLOYEES")) {
                break;
            }
            // skip headers/blank markers
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
        }

        // EMPLOYEE SCAN
        while (file.hasNext()) {
            String first = file.next();

            // stop if we reach orders
            if (first.equals("ORDERS")) {
                break;
            }

            String last = file.next();
            String username = file.next();
            String password = file.next();
            boolean manager = file.nextBoolean();

            Employee e = new Employee(first, last, username, password, manager);
            employees.add(e);
        }

        // ORDER SCAN
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
                    PCPart part = catalog.searchByPrimaryKey(sku);
                    if (part != null) {
                        items.addLast(part);
                    }
                }
                skuScan.close();
            }

            Customer temp = new Customer("", "", username, "", "", "", "", "");
            Customer customer = customers.get(temp);

            Order order = new Order(orderId, customer, items, shippingSpeed);
            orderHeap.insert(order);
            if (customer != null) {
                customer.addOrder(order);
            }
        }

        file.close();

        Scanner input = new Scanner(System.in);
        int choice = 0;

        while (choice != 4) {
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Continue as Guest");
            System.out.println("4. Quit");
            System.out.print("Enter choice: ");

            choice = readInt(input);

            if (choice == 1) {
                login(customers, employees, orderHeap, input);
            } else if (choice == 2) {
                createAccount(customers, input);
            } else if (choice == 3) {
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

    private static Comparator<Order> getOrderComparator() {
        return (a, b) -> {
            if (a == null && b == null) {
                return 0;
            }
            if (a == null) {
                return -1;
            }
            if (b == null) {
                return 1;
            }

            int speedCompare = Integer.compare(a.getShippingSpeed(), b.getShippingSpeed());
            if (speedCompare != 0) {
                return speedCompare;
            }

            // older order gets higher priority
            int ageCompare = Long.compare(b.getCreatedAtEpochMillis(), a.getCreatedAtEpochMillis());
            if (ageCompare != 0) {
                return ageCompare;
            }

            return Integer.compare(b.getOrderId(), a.getOrderId());
        };
    }

    private static int readInt(Scanner input) {
        while (!input.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            input.nextLine();
        }
        int value = input.nextInt();
        input.nextLine();
        return value;
    }

    /** login */
    public static void login(HashTable<Customer> customers,
                             HashTable<Employee> employees,
                             Heap<Order> orderHeap,
                             Scanner input) {

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
            customerInterface();

        } else if (foundEmployee != null && foundEmployee.passwordMatch(password)) {
            System.out.println("Employee login successful!");
            System.out.println(foundEmployee);
            employeeInterface(orderHeap, customers, input);

            if (foundEmployee.getIsManager()) {
                managerInterface();
            }

        } else {
            System.out.println("Invalid username or password.");
        }
    }

    /** createAccount */
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

    /** customerInterface */
    public static void customerInterface() {
        // TODO: customer menu still pending
    }

    /** guestInterface */
    public static void guestInterface() {
        // TODO: guest menu still pending
    }

    /** employeeInterface */
    public static void employeeInterface(Heap<Order> orderHeap,
                                         HashTable<Customer> customers,
                                         Scanner input) {
        int choice = 0;

        while (choice != 6) {
            System.out.println("\n=== Employee Menu ===");
            System.out.println("1. Search for an Order");
            System.out.println("2. View Order with Highest Priority");
            System.out.println("3. View All Orders Sorted by Priority");
            System.out.println("4. Ship an Order");
            System.out.println("5. Quit and Write to File");
            System.out.println("6. Return to Main Menu");
            System.out.print("Enter choice: ");
            choice = readInt(input);

            if (choice == 1) {
                System.out.println("1. Search by order id");
                System.out.println("2. Search by customer first and last name");
                System.out.print("Enter search type: ");
                int searchType = readInt(input);

                if (searchType == 1) {
                    System.out.print("Enter order id: ");
                    int orderId = readInt(input);
                    Order order = findOrderById(orderHeap, orderId);
                    if (order == null) {
                        System.out.println("Order not found.");
                    } else {
                        System.out.println(order);
                    }
                } else if (searchType == 2) {
                    System.out.print("Enter first name: ");
                    String first = input.nextLine();
                    System.out.print("Enter last name: ");
                    String last = input.nextLine();
                    printOrdersForCustomer(orderHeap, first, last);
                } else {
                    System.out.println("Invalid search option.");
                }
            } else if (choice == 2) {
                if (orderHeap.isEmpty()) {
                    System.out.println("No unshipped orders in queue.");
                } else {
                    System.out.println("Highest priority order:");
                    System.out.println(orderHeap.getMax());
                }
            } else if (choice == 3) {
                if (orderHeap.isEmpty()) {
                    System.out.println("No unshipped orders in queue.");
                } else {
                    ArrayList<Order> sorted = orderHeap.heapSortSnapshot();
                    System.out.println("Orders sorted by priority:");
                    for (int i = 0; i < sorted.size(); i++) {
                        System.out.println((i + 1) + ". " + sorted.get(i));
                    }
                }
            } else if (choice == 4) {
                if (orderHeap.isEmpty()) {
                    System.out.println("No orders available to ship.");
                } else {
                    Order shipped = orderHeap.extractMax();
                    shipped.setShipped(true);

                    Customer owner = shipped.getCustomer();
                    if (owner != null) {
                        owner.markOrderShipped(shipped.getOrderId());
                    }

                    System.out.println("Shipped order: " + shipped.getOrderId());
                }
            } else if (choice == 5) {
                writeEmployeeSnapshot(orderHeap, customers);
                System.out.println("Wrote employee snapshot to employee_orders_report.txt");
                choice = 6;
            } else if (choice == 6) {
                System.out.println("Returning to main menu...");
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static Order findOrderById(Heap<Order> orderHeap, int orderId) {
        for (int i = 1; i <= orderHeap.getHeapSize(); i++) {
            Order o = orderHeap.getElement(i);
            if (o != null && o.getOrderId() == orderId) {
                return o;
            }
        }
        return null;
    }

    private static void printOrdersForCustomer(Heap<Order> orderHeap, String first, String last) {
        boolean foundAny = false;
        for (int i = 1; i <= orderHeap.getHeapSize(); i++) {
            Order o = orderHeap.getElement(i);
            if (o == null || o.getCustomer() == null) {
                continue;
            }

            Customer c = o.getCustomer();
            if (c.getFirstName().equalsIgnoreCase(first)
                && c.getLastName().equalsIgnoreCase(last)) {
                System.out.println(o);
                foundAny = true;
            }
        }

        if (!foundAny) {
            System.out.println("No matching orders found for that customer.");
        }
    }


    private static void writeEmployeeSnapshot(Heap<Order> orderHeap, HashTable<Customer> customers) {
        try {
            java.io.PrintWriter out = new java.io.PrintWriter("employee_orders_report.txt");
            out.println("UNSHIPPED ORDERS (highest priority first)");
            ArrayList<Order> sorted = orderHeap.heapSortSnapshot();
            for (int i = 0; i < sorted.size(); i++) {
                out.println(sorted.get(i));
            }
            out.println();
            out.println("CUSTOMER COUNT: " + customers.getNumElements());
            out.close();
        } catch (java.io.IOException e) {
            System.out.println("Error writing employee snapshot: " + e.getMessage());
        }
    }

    /** managerInterface */
    public static void managerInterface() {
        // TODO: manager menu still pending
    }
}
