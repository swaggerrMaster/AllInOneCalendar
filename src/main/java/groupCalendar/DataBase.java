package groupCalendar;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DataBase {
    private static final String DB_URL = "jdbc:mysql://localhost/GroupCalendar?user=root&password=$";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * registerUser will add a new user to the data base
     * @param username
     * @param name
     * @param email
     * @param password
     * @return userID or negative int if error
     */
    public static int registerUser(String username, String name, String email, String password) {
        // Check if the username or email already exists
        if (usernameExists(username)) {
            return -1; // means username exists
        }
        if (emailExists(email)) {
            return -2; // means email already exists
        }

        // insert new user if username and email are both unique
        String sql = "INSERT INTO Users (username, name, email, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // return the new user ID
                    }
                }
            }
            return -3; // Some other insertion error
        } catch (SQLException e) {
            e.printStackTrace();
            return -3; // Database error during insertion
        }
    }
    
    /**
     * checks if username already exists
     * @param username
     * @return ture if username exists
     */
    private static boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * checks if email already exists
     * @param email
     * @return ture if email exists
     */
    private static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    /**
     * checks if users username and password exists
     * @param username
     * @param password
     * @return true if username and password is valid
     */
    public static boolean checkLogin(String username, String password) {
        String sql = "SELECT password FROM Users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    /**
     * inserts and event into the table
     * @param event
     * @return returns the event id and -1 if failed
     */
    public static int addEvent(Event event) {
    	
    	event.displayEventDetails();
    	
        String sql = "INSERT INTO Events (eventName, eventDate, startTime, endTime, eventLocation, description, userID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        	
            pstmt.setString(1, event.getEventName());
            pstmt.setDate(2, Date.valueOf(event.getEventDate()));
            pstmt.setTime(3, Time.valueOf(event.getStartTime()));
            pstmt.setTime(4, Time.valueOf(event.getEndTime()));
            pstmt.setString(5, event.getEventLocation());
            pstmt.setString(6, event.getDescription());
            pstmt.setInt(7, event.getUserID());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                    	// Return the newly created event ID
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Return -1 if the insertion failed
        return -1;
    }
   
    
    /**
     * gets a users userID from thier username
     * @param username
     * @return the userID
     */
    public static int getUserIDByUsername(String username) {
        String sql = "SELECT userID FROM Users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("userID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Return -1 if the user is not found or if an error occurs
    }
    
    
    /**
     * gets a users name from thier username
     * @param username
     * @return the name
     */
    public static String getNameByUsername(String username) {
        String sql = "SELECT name FROM Users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");  // Return the name found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no name is found or if an error occurs
    }
    
    
    /**
     * gets a users username from thier userID
     * @param userID
     * @return the username
     */
    public static String getUsernameByUserID(int userID) {
        String sql = "SELECT username FROM Users WHERE userID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");  // Return the username found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
     // Return null if no username is found or if an error occurs
        return null;
    }
    
    
    /**
     * retruns a list of all events in the database
     * @return list of events
     */
    public static List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Events";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	String username = getUsernameByUserID(rs.getInt("userID"));
                Event event = new Event(rs.getInt("eventID"),
                    rs.getString("eventName"),
                    rs.getDate("eventDate").toString(),
                    rs.getTime("startTime").toString(),
                    rs.getTime("endTime").toString(),
                    rs.getString("eventLocation"),
                    rs.getString("description"),
                    username,
                    rs.getInt("userID")
                );
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    
    /**
     * returns a list of all users
     * @return list of all users
     */
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT userID, email, name, username, password FROM Users";  // Adjust fields as necessary
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt("userID"), rs.getString("email"), rs.getString("name"), rs.getString("username"), rs.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    
    /**
     * returns list of events only for the userIDs that are passed int he parameter
     * @param userIDs
     * @return list of events
     */
    public static List<Event> getEventsByUserIDs(List<Integer> userIDs) {
        List<Event> events = new ArrayList<>();
        System.out.println("getEventsByUserIds function");
        for (int i = 0; i < userIDs.size(); i++) {
        	System.out.print(userIDs.get(i) + " ");
        }
        System.out.println();
        String sql = "SELECT * FROM Events WHERE userID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	for (int i = 0; i < userIDs.size(); i++) {
        		pstmt.setInt(1, userIDs.get(i)); // Setting the IN clause
            
               ResultSet rs = pstmt.executeQuery();
               while (rs.next()) {
               	String username = getUsernameByUserID(rs.getInt("userID"));
               	Event event = new Event(rs.getInt("eventID"),
                           rs.getString("eventName"),
                           rs.getDate("eventDate").toString(),
                           rs.getTime("startTime").toString(),
                           rs.getTime("endTime").toString(),
                           rs.getString("eventLocation"),
                           rs.getString("description"),
                           username,
                           rs.getInt("userID")
                       );
                   events.add(event);
               }
            
        	}
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    
    /**
     * deletes all events from events table associated witht his user and then deltesthe user from USER table
     * @param username
     * @return true if delete was successful
     */
    public static boolean deleteUser(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DriverManager.getConnection(DB_URL);
         // Disable auto-commit incase w ehave to udno something
            conn.setAutoCommit(false);

            // Get the userID from the username
            int userID = getUserIDByUsername(username);
            if (userID == -1) {
                conn.rollback();  // undo anything if user ID not valid
                return false;     // means user does not exist
            }

            // SQL to delete all events for the user
            String sqlDeleteEvents = "DELETE FROM Events WHERE userID = ?";
            pstmt = conn.prepareStatement(sqlDeleteEvents);
            pstmt.setInt(1, userID);
            // might delete 0 rows if no events, which is fine
            pstmt.executeUpdate();

            // SQL to delete the user
            String sqlDeleteUser = "DELETE FROM Users WHERE userID = ?";
            pstmt = conn.prepareStatement(sqlDeleteUser);
            pstmt.setInt(1, userID);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
            	// commit eveything
                conn.commit();
                // delete was successful, set success to true
                success = true;
                System.out.println("delte: " + success);
            }
            else {
            	// indo if no user was actually deleted
                conn.rollback();
                success = false;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                	// undo if there was an error
                	conn.rollback();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                	pstmt.close();
                }
                if (conn != null) {
                	// reser default detting for commit
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("delte: " + success);
        return success;
    }

}
