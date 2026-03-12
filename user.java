public abstract class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    //getters
    public String getFirstName() {
        return firstName;
    
    }

    public String getLastName() {
        return lastName;

    }

    public String getEmail() {
        return email;

    }

    public String getPassword() { 
        return password;

    }

    //setters dont know if well need
    public void setEmail(String email) {
        this.email = email;

    }

    public void setPassword(String password) {
        this.password = password;

    }


    //other methods
    public boolean passwordMatch(String pw) { // use for password match at login
        if (pw.equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    @Override public String toString() {
        return "First Name: " +  firstName 
            + "Last Name:  " + lastName 
            + "email: " + email; // no password yet for security
    }

    @Override public boolean equals(Object obj) { // for hastable to check login
        if (this == obj) {
            return true;
        } else if (!(obj instanceof User)) {
            return false;
        } else {
            User user = (User) obj;
            return email.equals(user.email); // check the email equal
        }
    }

    @Override public int hashCode() { // override hasCode to hash email
        return email.hasCode();
    }


}