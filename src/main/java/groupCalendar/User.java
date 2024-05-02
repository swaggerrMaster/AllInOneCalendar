package groupCalendar;

public class User {
    private int userID;
    private String email;
    private String name;
    private String username;
    private String password;

    // Constructor
    public User(int userID, String email, String name, String username, String password) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // to print out evything
    public void displayUserDetails() {
        System.out.println("User ID: " + userID);
        System.out.println("Email: " + email);
        System.out.println("Username: " + username);
    }
}

