public class Customer extends User {
    private String address;
    private String city;
    private String state;
    private String zip; 
    private LinkedList<Order> shippedOrders; 
    private LinkedList<Order> unshippedOrders;
   
    public Customer(String firstName, String lastName, String username, String password, 
        String address, String city, String state, String zip) {

        super(firstName, lastName, username, password);
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;

        shippedOrders = new LinkedList<>();
        unshippedOrders = new LinkedList<>();
    }

    /** Links a new order to this customer (placed in the unshipped queue). */
    public void addOrder(Order order) {
        if (order != null) {
            unshippedOrders.addLast(order);
        }
    }

    /** Moves an order from unshipped to shipped and marks it as shipped. */
    public void markOrderShipped(int orderId) {
        unshippedOrders.positionIterator();
        while (!unshippedOrders.offEnd()) {
            Order o = unshippedOrders.getIterator();
            if (o.getOrderId() == orderId) {
                o.setShipped(true);
                unshippedOrders.removeIterator();
                shippedOrders.addLast(o);
                return;
            }
            unshippedOrders.advanceIterator();
        }
    }

    public LinkedList<Order> getShippedOrders() {
        return shippedOrders;
    }

    public LinkedList<Order> getUnshippedOrders() {
        return unshippedOrders;
    }

    /** Prints all orders for this customer with their shipping status. */
    public void viewOrders() {
        System.out.println("=== Orders for " + super.toString() + " ===");
        System.out.println("-- Unshipped --");
        unshippedOrders.positionIterator();
        while (!unshippedOrders.offEnd()) {
            System.out.println(unshippedOrders.getIterator());
            unshippedOrders.advanceIterator();
        }
        System.out.println("-- Shipped --");
        shippedOrders.positionIterator();
        while (!shippedOrders.offEnd()) {
            System.out.println(shippedOrders.getIterator());
            shippedOrders.advanceIterator();
        }
    }

    @Override public String toString() {
        return super.toString() +
           "\nAddress: " + address +
           "\nCity: " + city +
           "\nState: " + state +
           "\nZip: " + zip;
    }

}