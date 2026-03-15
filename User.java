public abstract class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    //getters
    public String getFirstName() {
        return firstName;
    
    }

    public String getLastName() {
        return lastName;

    }

    public String getUsername() {
        return username;

    }

    public String getPassword() { 
        return password;

    }

    //setters dont know if well need
    public void setUsername(String username) {
        this.username = username;

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
            + "Username: " + username; // no password yet for security
    }

    @Override public boolean equals(Object obj) { // for hastable to check login
        if (this == obj) {
            return true;
        } else if (!(obj instanceof User)) {
            return false;
        } else {
            User user = (User) obj;
            return this.username.equals(user.username) &&
                this.password.equals(user.password); // check the username and pw equal
        }
    }

    @Override public int hashCode() { // override hasCode to hash username
        return (username + password).hashCode();
    }


}