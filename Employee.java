public class Employee extends User {
    private boolean isManager;

    public Employee(String firstName, String lastName, String username, 
        String password, boolean isManager) {
        super(firstName, lastName, username, password);
        this.isManager = isManager;
        
    }

    //getter
    public boolean getIsManager() {
        return isManager;
    
    }

    //setter
    public void setIsManager(boolean isManager) {
        this.isManager = isManager;

    }

    //other methods
    public String toString() {
        String result = super.toString();
        String manager = "";
        if (isManager == true) {
            result += "Manager: yes";
        } else {
            result += " Manager: no";
        }

        return result;
    }


}