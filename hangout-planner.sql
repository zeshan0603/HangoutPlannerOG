CREATE DATABASE hangout_planner;

USE hangout_planner;

CREATE TABLE USERS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    isLoggedIn BOOLEAN DEFAULT FALSE,
    isSignedUp BOOLEAN DEFAULT TRUE,
    time TIME DEFAULT NULL
);

-- Insert default values
INSERT INTO USERS (name, lastName, email, password, isLoggedIn, isSignedUp, time)
VALUES
    ('Alex', 'Joeseph', 'user1@gmail.com', 'user1', FALSE, TRUE, NULL),
    ('Zeshan', 'Merchant', 'user2@gmail.com', 'user2', FALSE, TRUE, NULL),
    ('Jackson', 'Jonas', 'user3@gmail.com', 'user3', FALSE, TRUE, NULL),
    ('Sub', 'Roza', 'user4@gmail.com', 'user4', FALSE, TRUE, NULL);

CREATE TABLE SLOTS (
    startTime TIME NOT NULL,
    endTime TIME NOT NULL
);

-- Insert default slot values
INSERT INTO SLOTS (startTime, endTime)
VALUES
    ('12:00', '14:00'),
    ('14:00', '16:00'),
    ('16:00', '18:00'),
    ('18:00', '20:00'),
    ('20:00', '22:00');
