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
        this.state = state;
        this.zip = zip;
        
        shippedOrders = new LinkedList<>();
        unshippedOrders = new LinkedList<>();
    }

    @Override public String toString() {
        return super.toString() +
           "\nAddress: " + address +
           "\nCity: " + city +
           "\nState: " + state +
           "\nZip: " + zip;
    } 

}