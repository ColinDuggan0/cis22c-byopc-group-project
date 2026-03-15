import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        String username;
        String password;
        HashTable<Customer> customers = new HashTable<>(10);
        HashTable<Employee> employees = new HashTable<>(10);

        ///////////// USER LOGIN PRINT OUTS ////////////////
        //dummy users for testing
        customers.add(new Customer("Alice", "Smith", "alice@email.com", "1234", 
            "123 Main St", "San Jose", "CA", "95112"));
        employees.add(new Employee("Bob", "Jones", "bob@email.com", "abcd", true));

        Scanner input = new Scanner(System.in);

        System.out.println("1. Login");
        System.out.println("2. Create Account");
        System.out.print("Enter choice: ");

        int choice = input.nextInt();
        input.nextLine();

        //lets use 3 differend methods to separate the employee, guest, and customer menu options
        //print out 3 files one for customer.txt, employee.txt, manager.txt or something

        if (choice == 1) { 
            login(customers, employees, input);
        } else if (choice == 2) {
            //create an account 
            //make sure to add new account to file! - hashtable tostring - we will print the hashtable with printWriter
            createAccount(customers, input);
        } else if (choice == 3) {
            System.out.println("\nLogged in as Guest.");
            //guest menu options
        }

    }

    public static void login(HashTable<Customer> customers, HashTable<Employee> employees, Scanner input) {

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

        } else if (foundEmployee != null && foundEmployee.passwordMatch(password)) {
            System.out.println("Employee login successful!");
            System.out.println(foundEmployee);

        } else {
            System.out.println("Invalid username or password.");
        }
    }

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
        //should we check if the username alredy exist or not? yes
        //are we allow to create an account for employee? no 

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

    

    




}