import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        String username;
        String password;
        HashTable<User> users = new HashTable<>(10);

        ///////////// USER LOGIN PRINT OUTS ////////////////
        //dummy users for testing
        users.add(new Customer("Alice", "Smith", "alice@email.com", "1234"));
        users.add(new Employee("Bob", "Jones", "bob@email.com", "abcd", true)); 

        Scanner input = new Scanner(System.in);

        System.out.println("1. Login");
        System.out.println("2. Create Account");
        System.out.print("Enter choice: ");

        int choice = input.nextInt();
        input.nextLine();

        if (choice == 1) {
            login(users, input);
        } else if (choice == 2) {
            //create an account
        }

    }

    public static void login(HashTable<User> users, Scanner input) {

        System.out.print("Enter email: ");
        String email = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        User temp = new Customer("", "", email, ""); // check temp user with same email

        User found = users.get(temp); // use hastable to find user with the matching email

        if (found != null && found.passwordMatch(password)) {
            System.out.println("Login successful!");
            System.out.println(found); // tostring
        } else {
            System.out.println("Invalid email or password.");
        }

    }




}