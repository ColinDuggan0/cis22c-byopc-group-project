import java.util.Scanner;

/** UI helper methods for order-related employee/manager menus. */
public class OrderUI {

    /** Nested search menu: search by order id or by customer name. */
    public static void searchForOrder(Heap<Order> orderHeap, Scanner input) {
        int searchChoice = -1;
        while (searchChoice != 3) {
            System.out.println("\n--- Search for an Order ---");
            System.out.println("1. Search by order id");
            System.out.println("2. Search by customer first and last name");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");
            searchChoice = input.nextInt();
            input.nextLine();

            switch (searchChoice) {
                case 1:
                    OrderService.viewOrderByOrderId(orderHeap, input);
                    break;
                case 2:
                    OrderService.viewOrdersByCustomerName(orderHeap, input);
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

