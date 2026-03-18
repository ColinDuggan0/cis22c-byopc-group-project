public class Order {
   private int orderId;
   private Customer customer;
   private LinkedList<PCPart> lineItems;
   private int shippingSpeed; // 3=overnight, 2=rush, 1=standard
   private long createdAtEpochMillis;
   private int priorityScore;
   private boolean shipped;

   /** Full constructor — pass an existing list of parts, or null to start empty. */
   public Order(int orderId, Customer customer, LinkedList<PCPart> lineItems, int shippingSpeed) {
      this.orderId = orderId;
      this.customer = customer;
      this.lineItems = (lineItems == null) ? new LinkedList<PCPart>() : lineItems;
      this.shippingSpeed = shippingSpeed;
      this.createdAtEpochMillis = System.currentTimeMillis();
      this.priorityScore = computePriorityScore();
      this.shipped = false;
   }

   /** Convenience constructor — start with an empty cart and add parts via addItem(). */
   public Order(int orderId, Customer customer, int shippingSpeed) {
      this(orderId, customer, null, shippingSpeed);
   }

   /** Adds a single PCPart to this order (for individual part purchases). */
   public void addItem(PCPart part) {
      if (part != null) {
         lineItems.addLast(part);
      }
   }

   public int computePriorityScore() {
      // Age bonus: older orders get a small bump within the same shipping tier.
      // Capped at 9999 (~7 days) so shipping speed (×1,000,000) always dominates.
      int ageBonus = (int) Math.min(9999L,
            Math.max(0L, (System.currentTimeMillis() - createdAtEpochMillis) / 60000L));
      priorityScore = (shippingSpeed * 1000000) + ageBonus;
      return priorityScore;
   }

   public int getOrderId() {
      return orderId;
   }

   public Customer getCustomer() {
      return customer;
   }

   public LinkedList<PCPart> getLineItems() {
      return lineItems;
   }

   public int getShippingSpeed() {
      return shippingSpeed;
   }

   public long getCreatedAtEpochMillis() {
      return createdAtEpochMillis;
   }

   public int getPriorityScore() {
      return priorityScore;
   }

   public boolean isShipped() {
      return shipped;
   }

   public void setShipped(boolean shipped) {
      this.shipped = shipped;
   }

   private String getShippingType() {
      if (shippingSpeed == 3) {
         return "overnight";
      } else if (shippingSpeed == 2) {
         return "rush";
      }
      return "standard";
   }

   @Override
   public String toString() {
      return "Order{" +
            "orderId=" + orderId +
            ", shippingType='" + getShippingType() + '\'' +
            ", status=" + (shipped ? "Shipped" : "Not Shipped") +
            ", priorityScore=" + priorityScore +
            ", customer=" + customer +
            '}';
   }
}
