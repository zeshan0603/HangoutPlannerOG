# HANGOUT PLANNER

Hangout Planner is a simple Java application for scheduling hangout times with your friends. It allows you to add, update, and generate the best meeting times based on your availability. 

This README will guide you on how to set up and use the project.

Setup
Download SQL Connector Jar:

Before you begin, you'll need to add the SQL Connector JAR file to your project in your preferred IDE, such as IntelliJ IDEA.

Download the SQL Connector JAR file compatible with your MySQL version. In this project, we used MySQL8.1.

I have added the connector jar file in this repository. You can also download it online.

In IntelliJ IDEA, go to File > Project Structure.

Under Project Settings, select Modules.

Click on the Dependencies tab and add the downloaded JAR file.

Configure Database Connection:

Open the DBConnection.java file and ensure that you set the correct database URL for your MySQL database. Also, make sure you have created a MySQL database with the required tables.

private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/your_database_name?useSSL=false";

Replace your_database_name with the name of your MySQL database.

Run SQL Queries:

Execute the SQL queries provided in the hangout-planner.sql file to set up the necessary database tables and default users. You can use a SQL command-line interface or a database management tool of your choice to run these queries.


source path_to_file/hangout-planner.sql;

Usage

The project includes four default users with email user1@gmail.com and passwords from user1 to user4. You can use these users to log in and test the application.

If you want to remove the default users, run the following SQL query:

DELETE FROM USERS;

To add new users, use the signup method, and when they log in, the isLogged column in the database will be set to 1. When they log out, it will be set to 0. After each operation, you can check the changes in the database by running:

SELECT * FROM USERS;

Use the following options in the application:

Add Time: Allows you to add your available time slots.

Update Time: Update your available time slots.

View All User Times: Display the available time slots of all users.

Generate Best Time: Find the best meeting times based on user availability.


Enjoy using Hangout Planner to simplify scheduling hangout times with your friends!


Feel free to contribute to this project or report any issues you encounter. Happy hangouting!

Please replace your_database_name with the actual name of your MySQL database in the DATABASE_URL. Also, replace path_to_file in the SQL query with the actual path to your database_queries.sql file.
