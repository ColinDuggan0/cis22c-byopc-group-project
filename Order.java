public class Order {
   private int orderId;
   private Customer customer;
   private LinkedList<PCPart> lineItems;
   private int shippingSpeed; // 3=overnight, 2=rush, 1=standard
   private long createdAtEpochMillis;
   private int priorityScore;

   public Order(int orderId, Customer customer, LinkedList<PCPart> lineItems, int shippingSpeed) {
      this.orderId = orderId;
      this.customer = customer;
      this.lineItems = (lineItems == null) ? new LinkedList<PCPart>() : lineItems;
      this.shippingSpeed = shippingSpeed;
      this.createdAtEpochMillis = System.currentTimeMillis();
      this.priorityScore = computePriorityScore();
   }

   public int computePriorityScore() {
      int cappedAgeMinutes = (int) Math.min(999999L,
            Math.max(0L, (System.currentTimeMillis() - createdAtEpochMillis) / 60000L));
      priorityScore = (shippingSpeed * 1000000) + cappedAgeMinutes;
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
            ", priorityScore=" + priorityScore +
            ", customer=" + customer +
            '}';
   }
}
