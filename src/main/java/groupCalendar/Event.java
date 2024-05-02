package groupCalendar;

public class Event {
    private int eventID;
    private String eventName;
    private String eventDate;
    private String startTime;
    private String endTime;
    private String eventLocation;
    private String description;
    private String username;
    private int userID;

    // Constructor
    public Event(int eventID, String eventName, String eventDate, String startTime, 
                 String endTime, String eventLocation, String description, String username, int userID) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventLocation = eventLocation;
        this.description = description;
        this.username = username;
        this.userID = userID;
    }

    // Getters and Setters
    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    // to print out stuff
    public void displayEventDetails() {
        System.out.println("Event ID: " + eventID);
        System.out.println("Event Name: " + eventName);
        System.out.println("Event Date: " + eventDate);
        System.out.println("Start Time: " + startTime);
        System.out.println("End Time: " + endTime);
        System.out.println("Location: " + eventLocation);
        System.out.println("Description: " + description);
        System.out.println("Created by Username: " + username);
    }
}


