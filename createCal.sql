CREATE DATABASE GroupCalendar;
USE GroupCalendar;

CREATE TABLE Users (
    userID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create the Events table
CREATE TABLE Events (
    eventID INT AUTO_INCREMENT PRIMARY KEY,
    eventName VARCHAR(255) NOT NULL,
    eventDate DATE NOT NULL,
    startTime TIME NOT NULL,
    endTime TIME NOT NULL,
    eventLocation VARCHAR(255) NULL,
    description TEXT NULL,
    userID INT,
    FOREIGN KEY (userID) REFERENCES Users(userID)
);