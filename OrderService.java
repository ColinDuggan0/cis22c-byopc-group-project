import java.util.Scanner;

public class OrderService {

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

    public static void viewOrdersByCustomerName(Heap<Order> orderHeap, Scanner input) {
        System.out.print("Enter customer first name: ");
        String first = input.nextLine().trim();

        System.out.print("Enter customer last name: ");
        String last = input.nextLine().trim();

        boolean found = false;

        // Traverse all orders without mutating the heap
        ArrayList<Order> allOrders = orderHeap.heapSortSnapshot();

        for (int i = 0; i < allOrders.size(); i++) {
            Order o = allOrders.get(i);

            Customer c = o.getCustomer();
            if (c == null) continue;

            boolean firstMatch = c.getFirstName().equalsIgnoreCase(first);
            boolean lastMatch = c.getLastName().equalsIgnoreCase(last);

            if (firstMatch && lastMatch) {
                found = true;
                System.out.println(o);
            }
        }

        if (!found) {
            System.out.println("No orders found for " + first + " " + last + ".");
        }
    }

    public static void viewOrderByOrderId(Heap<Order> orderHeap, Scanner input) {
        System.out.print("Enter order id: ");
        int id = input.nextInt();
        input.nextLine();

        ArrayList<Order> allOrders = orderHeap.heapSortSnapshot();

        boolean found = false;

        for (int i = 0; i < allOrders.size(); i++) {
            Order o = allOrders.get(i);
            if (o.getOrderId() == id) {
                found = true;
                System.out.println(o);
                break;
            }
        }

        if (!found) {
            System.out.println("No order found with id " + id + ".");
        }
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

    public static void viewHighestPriorityOrder(Heap<Order> orderHeap) {
        if (orderHeap.isEmpty()) {
            System.out.println("No orders in the queue.");
            return;
        }
        System.out.println("Highest priority order:");
        System.out.println(orderHeap.getMax());
    }

    public static void viewAllOrdersSortedByPriority(Heap<Order> orderHeap) {
        if (orderHeap.isEmpty()) {
            System.out.println("No orders in the queue.");
            return;
        }

        ArrayList<Order> sorted = orderHeap.heapSortSnapshot();
        System.out.println("All orders sorted by priority (highest to lowest):");
        for (int i = 0; i < sorted.size(); i++) {
            System.out.println(sorted.get(i));
        }
    }
}

