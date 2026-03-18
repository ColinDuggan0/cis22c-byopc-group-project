import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class PersistenceService {

    /** Writes current customer info and their shipped/unshipped orders to a file (customer interface quit). */
    public static void saveCustomerAndOrderInfo(Customer currentCustomer) {
        String filename = "customer_" + currentCustomer.getUsername() + "_orders.txt";
        try (PrintWriter out = new PrintWriter(new File(filename))) {
            out.println(currentCustomer);
            out.println("-- Unshipped --");
            writeOrderList(out, currentCustomer.getUnshippedOrders());
            out.println("-- Shipped --");
            writeOrderList(out, currentCustomer.getShippedOrders());
            System.out.println("Customer and order info written to " + filename);
        } catch (Exception e) {
            System.out.println("Error writing: " + e.getMessage());
        }
    }

    private static void writeOrderList(PrintWriter out, LinkedList<Order> list) {
        list.positionIterator();
        while (!list.offEnd()) {
            out.println(list.getIterator());
            list.advanceIterator();
        }
    }

    /**
     * Writes customers, employees, and orders back to users_upload.txt,
     * and writes the catalog back to pc_parts_products.csv.
     */
    public static void saveAllState(
        LinkedList<Customer> customerList,
        LinkedList<Employee> employeeList,
        CatalogService catalog
    ) {
        try (PrintWriter out = new PrintWriter(new File("users_upload.txt"))) {
            out.println("CUSTOMERS");
            customerList.positionIterator();
            while (!customerList.offEnd()) {
                Customer c = customerList.getIterator();
                out.print(safeToken(c.getFirstName())); out.print(" ");
                out.print(safeToken(c.getLastName())); out.print(" ");
                out.print(safeToken(c.getUsername())); out.print(" ");
                out.print(safeToken(c.getPassword())); out.print(" ");
                out.print(safeToken(c.getAddress())); out.print(" ");
                out.print(safeToken(c.getCity())); out.print(" ");
                out.print(safeToken(c.getState())); out.print(" ");
                out.println(safeToken(c.getZip()));
                customerList.advanceIterator();
            }
            out.println();

            out.println("EMPLOYEES");
            employeeList.positionIterator();
            while (!employeeList.offEnd()) {
                Employee e = employeeList.getIterator();
                out.print(safeToken(e.getFirstName())); out.print(" ");
                out.print(safeToken(e.getLastName())); out.print(" ");
                out.print(safeToken(e.getUsername())); out.print(" ");
                out.print(safeToken(e.getPassword())); out.print(" ");
                out.println(Boolean.toString(e.getIsManager()));
                employeeList.advanceIterator();
            }
            out.println();

            out.println("ORDERS");
            customerList.positionIterator();
            while (!customerList.offEnd()) {
                Customer c = customerList.getIterator();
                writeOrdersForCustomer(out, c.getUnshippedOrders());
                out.println();
                writeOrdersForCustomer(out, c.getShippedOrders());
                out.println();
                customerList.advanceIterator();
            }
        } catch (Exception e) {
            System.out.println("Error writing users_upload.txt: " + e.getMessage());
        }

        try {
            catalog.writeToCsv(Paths.get("pc_parts_products.csv"));
        } catch (IOException e) {
            System.out.println("Error writing pc_parts_products.csv: " + e.getMessage());
        }
    }

    private static void writeOrdersForCustomer(PrintWriter out, LinkedList<Order> orders) {
        orders.positionIterator();
        while (!orders.offEnd()) {
            Order o = orders.getIterator();
            if (o != null && o.getCustomer() != null) {
                out.print(o.getOrderId()); out.print(" ");
                out.print(safeToken(o.getCustomer().getUsername())); out.print(" ");
                out.print(o.getShippingSpeed());

                LinkedList<PCPart> items = o.getLineItems();
                if (items != null) {
                    items.positionIterator();
                    while (!items.offEnd()) {
                        PCPart p = items.getIterator();
                        if (p != null) {
                            out.print(" ");
                            out.print(safeToken(p.getSku()));
                        }
                        items.advanceIterator();
                    }
                }
                out.println();
            }
            orders.advanceIterator();
        }
    }

    private static String safeToken(String raw) {
        if (raw == null) return "";
        return raw.trim().replace(" ", "_");
    }
}

