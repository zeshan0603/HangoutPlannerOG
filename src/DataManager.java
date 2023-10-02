
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;



public class DataManager {

    public static Connection con;
    private static final Scanner in = new Scanner(System.in);
    public static ArrayList<Users>  usersList = new ArrayList<>();
    public static ArrayList<Slot> slotDataList = new ArrayList<Slot>();
    int [] slotsCounter = new int[slotDataList.size()];
    public static ArrayList<Slot> bestSlots = new ArrayList<Slot>();

    static int count = 0;


    public static boolean logIn(String logInEmail, String logInPassword) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet result = null;

        try {
            con = DBConnection.createConnection();
            String selectQuery = "SELECT * FROM users WHERE EMAIL=? AND PASSWORD=?";
            pstmt = con.prepareStatement(selectQuery);
            pstmt.setString(1, logInEmail);
            pstmt.setString(2, logInPassword);
            result = pstmt.executeQuery();

            if (result.next()) {
                // Update the isLoggedIn column to true
                String updateQuery = "UPDATE users SET isLoggedIn = true WHERE email = ?";
                pstmt = con.prepareStatement(updateQuery);
                pstmt.setString(1, logInEmail);
                pstmt.executeUpdate();

                System.out.println("Logged In Successfully");
                System.out.println("WELCOME " + result.getString("name"));

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }


//    public static PreparedStatement pstmtCreator(String query){
//        PreparedStatement pstmt = con.prepareStatement(query);
//        return pstmt;
//    }



    public static boolean emailValidator(String email){
        // A simple pattern to check for email format with @ and a domain
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
    public static boolean passwordValidator(String password) {
        // Minimum password length
        int minLength = 8;
        if (password.length() < minLength) {
            return false;
        }
        boolean hasUppercase = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
                break;
            }
        }
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
                break;
            }
        }
        return hasUppercase && hasDigit;
    }



    public static String takePasswordInput(){
        String password;
        while(true){
            System.out.print("Enter your password : ");
            password = in.nextLine();
            if(passwordValidator(password)){
                break;
            } else {
                System.out.println("Password should be 8 char long, contain 1 uppercase and 1 number");
            }
        }
        return password;
    }

    public static String takeEmailInput(){
        String email;
        while (true) {
            System.out.print("Enter your email : ");
            email = in.nextLine();
            if (emailValidator(email)) {
                break; // Exit the loop if the email is valid
            } else {
                System.out.println("Invalid email format. Please enter a valid email address.");
            }
        }
        return email;
    }
    public static void signUp() {
        System.out.println("ADDING NEW USER");
        System.out.print("Enter your name : ");
        String name = in.nextLine();
        System.out.print("Enter your last name : ");
        String lastName = in.nextLine();
        //email
        String email = takeEmailInput();
        //password
        String password = takePasswordInput();

        System.out.print("Confirm password : ");
        String confirmPassword = in.nextLine();
        if (password.equals(confirmPassword)) {
            try {
                con = DBConnection.createConnection();
                int userId = addUserToDatabase(con, name, lastName, email, password);
                if (userId > 0) {
                    System.out.println("USER CREATED WITH ID " + userId);
                } else {
                    System.out.println("User creation failed.");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("Email id already exists");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Passwords do not match, please try again!");
        }
    }

    private static int addUserToDatabase(Connection con, String name, String lastName, String email, String password) throws SQLException {
        String insertQuery = "INSERT INTO users (name, lastname, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStatement = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1, name);
            insertStatement.setString(2, lastName);
            insertStatement.setString(3, email);
            insertStatement.setString(4, password);
            int affectedRows = insertStatement.executeUpdate();
            if (affectedRows > 0) {
                String updateQuery = "UPDATE users SET isSignedUp = true WHERE email = ?";
                PreparedStatement pstmt = con.prepareStatement(updateQuery);
                pstmt.setString(1, email);
                pstmt.executeUpdate();
                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1; // Return -1 to indicate failure
    }



    /*ADD TIME METHOD
      This method takes a user id and if that id is found in the array list, it will add the time else it will print
      user not found.
      * */
    public static void addTime(String email, LocalTime newTime) {
        Connection con;
        PreparedStatement pstmt;
        ResultSet result;

        try {
            con = DBConnection.createConnection();

            // Check if the time is already set for the user
            String checkTimeQuery = "SELECT time FROM users WHERE email = ?";
            pstmt = con.prepareStatement(checkTimeQuery);
            pstmt.setString(1, email);
            result = pstmt.executeQuery();

            if (result.next()) {
                // If the time is not null, display a message and return
                if (result.getTime("time") != null) {
                    System.out.println("TIME IS ALREADY ADDED, PLEASE UPDATE IT");
                    return;
                }
            }
            // If the time is null or not set, update it
            String updateTimeQuery = "UPDATE users SET time = ? WHERE email = ?";
            pstmt = con.prepareStatement(updateTimeQuery);
            pstmt.setTime(1, Time.valueOf(newTime));
            pstmt.setString(2, email);
            pstmt.executeUpdate();

            System.out.println("Time added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void updateTime(String email) {
        Connection con;
        PreparedStatement pstmt;
        ResultSet result;
        try {
            con = DBConnection.createConnection();
            // Check if the time is already set for the user
            String checkTimeQuery = "SELECT time FROM users WHERE email = ?";
            pstmt = con.prepareStatement(checkTimeQuery);
            pstmt.setString(1, email);
            result = pstmt.executeQuery();

            if (result.next()) {
                // If the time is not null, display a message and return
                if (result.getTime("time") == null) {
                    System.out.println("Please add time first!");
                    return;
                }
            }
            // If the time is null or not set, update it
            String updateTimeQuery = "UPDATE users SET time = ? WHERE email = ?";
            pstmt = con.prepareStatement(updateTimeQuery);
            System.out.println("Enter the new time(HH:mm)");
            System.out.print("NOTE: The time should be between 12:00PM to 8:00PM : ");
            LocalTime newTime = takeTimeInput();
            pstmt.setTime(1, Time.valueOf(newTime));
            pstmt.setString(2, email);
            pstmt.executeUpdate();

            System.out.println("Time updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static LocalTime takeTimeInput() {
        while (true) {
            String updatedTimeInput = in.nextLine();
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
            try {
                LocalTime parsedTime = LocalTime.parse(updatedTimeInput, formatter1);
                // Check if the entered time is within the specified range
                if (parsedTime.isAfter(LocalTime.of(11, 59)) && parsedTime.isBefore(LocalTime.of(20, 0))) {
                    return parsedTime;
                } else {
                    System.out.print("Please enter a time between 12:00PM to 8:00PM : ");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Please enter the time in the correct format (HH:mm)");
            }
        }
    }

    public void displayAllTime(){
        Connection con;
        PreparedStatement pstmt;
        ResultSet result;

        ArrayList<Users> userDataList = fetchUserData();


        for (Users users : userDataList) {
            if (users.getTime() == null) {
                System.out.println(users.getName() + " has not added a time yet");
            } else {
                System.out.println(users.getName() + "'s time is " + users.getTime());
            }
        }
    }
    public void addUser(Users newUser) {
        if(!checkSize())
        {
            count++;
            usersList.add(newUser);
        }
    }
    public boolean checkSize() {
        return usersList.size() >= 10;
    }

    public int checkRealSize() {

        return usersList.size();
    }

    public Users getUser(String name, String lastName) {
        int j = 0;
        for (int i = 0; i < usersList.size() - 1; i++) {
            if(usersList.get(i).getName().equals(name) && usersList.get(i).getLastName().equals(lastName)) j = i;
        }

        return usersList.get(j);
    }

    public Users getUser(int id) {

        for (Users users : usersList) {
            if (users.getId() == id) return users;
        }
        return null;
    }

    public void toStringList() {
        for (Users users : usersList) {
            System.out.println(users.getName() + " " +
                    users.getId());
        }
    }


    /*
    How generateBestTime works
    generate best time takes a ArrayList of users
    we take all the time from this usersList and add it to an ArrayList of LocalTime called userTimes
    we parse the user time which is a string and add it to userTimes
    we set a int userAddedTimeCount to 0 which will check how many users have added their time.
    for this method to work, atleast two users should add their time.

    we have a class called Slot which has a startTime, endTime and a counter variable set to 0
    We have a slotList with Slot objects which define different slots with different ranges(startTime,endTime)
    we iterate over the slotList using for each, we store each slot in each iteration.
    we take start and end time and store in local vars of the slot which we are currently iterating over

    now we iterate over the userTimes list, we check that whether the userTime fall under this slot
    if true then we increase the slotsCounter[i] ++;
    index i explaination is below

    this way we iterate over all slots and all the times

    now we simply iterate over the slotCounter array to find a max slot

    we then create a new arraylist of bestSlots
    we iterate over the slots counter,
    if the maxCount and slotCounter[i] matches, we will add that slot inside our best slot using index i
    bestSlot.add(slotList.get(i))

    finally we will display all the best slots
    * */

    public static void generateBestTime() {
        int userAddedTimeCount = 0;

        // Retrieve user data from the database
        ArrayList<Users> userDataList = fetchUserData();

        // Retrieve slot data from the database
        ArrayList<Slot> slotDataList = fetchSlotData();

        // Create an ArrayList to store LocalTime objects for users with added time
        ArrayList<LocalTime> userTimes = new ArrayList<>();
        for (Users user : userDataList) {
            if (user.getTime() != null) {
                userAddedTimeCount++;
                userTimes.add(LocalTime.parse(user.getTime(), DateTimeFormatter.ofPattern("HH:mm")));
            }
        }
        if (userAddedTimeCount >= 2) {
            int[] slotsCounter = new int[DataManager.slotDataList.size()];

            for (int i = 0; i < DataManager.slotDataList.size(); i++) {
                Slot slot = DataManager.slotDataList.get(i);
                LocalTime startTime = slot.getStartTime();
                LocalTime endTime = slot.getEndTime();

                for (LocalTime userTime : userTimes) {
                    // Check if userTime falls within the slot's time range
                    if (!userTime.isBefore(startTime) && userTime.isBefore(endTime)) {
                        slotsCounter[i]++; // index of slot counter and slotList are same because
                        // length of slotsCounter array is slotList.size
                    }
                }
            }
            int maxCount = -1;
            // Find the maximum count
            for (int count : slotsCounter) {
                if (count > maxCount) {
                    maxCount = count;
                }
            }
            // Add slots with the maximum count to the bestSlots list
            for (int i = 0; i < slotsCounter.length; i++) {
                if (slotsCounter[i] == maxCount) {
                    bestSlots.add(DataManager.slotDataList.get(i));
                    /* here we use the same index for slotsCounter and slotList due to two reason.
                    first is that they have same length
                    second reason is that when we were iterating over slotList to check user Time falls under a slot or not,
                    we had the same index. and then we increased the value of the integer stored on the index of slotCounter array

                    it looks something like this

                       0     1    2     3     4
                    {slot1,slot2,slot3,slot4,slot5} slotList
                    { 0,     0,    0,    0,     0  } slotCounter

                    so mapping is done here with using the same index

                    now if we have 2 slots with same counter, we will take their index, and use that index to get the slot from
                    slotList and add it to bestSlotList.

                    * */
                }
            }
            if (!bestSlots.isEmpty()) {
                System.out.println("Best Times to Meet (with count " + maxCount + "):");
                for (Slot bestSlot : bestSlots) {
                    System.out.println(bestSlot.getStartTime() + " - " + bestSlot.getEndTime());
                }
            } else {
                System.out.println("No suitable meeting time found.");
            }
        } else {
            System.out.println("At least two users should have added the time");
        }
    }
    public static ArrayList<Users> fetchUserData() {
        ArrayList<Users> fetchedList = new ArrayList<>();
        Connection con;
        PreparedStatement pstmt;
        ResultSet result;

        try {
            con = DBConnection.createConnection();
            String query = "SELECT * FROM USERS";
            pstmt = con.prepareStatement(query);
            result = pstmt.executeQuery();
            while (result.next()) {
                // Retrieve data from the result set for each row
                String name = result.getString("name");
                String lastName = result.getString("lastname");
                String email = result.getString("email");
                String password = result.getString("password");
                Time timeValue = result.getTime("time");

                // Handle null time values by setting a default value (e.g., 00:00)
                LocalTime time = (timeValue != null) ? timeValue.toLocalTime() : LocalTime.of(0, 0);

                // Add a new Users object to the fetchedList
                fetchedList.add(new Users(name, lastName, email, password, time));
            }
            // Close resources
            result.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fetchedList;
    }


    public static ArrayList<Slot> fetchSlotData(){
        ArrayList<Slot> fetchedList = new ArrayList<>();
        Connection con;
        PreparedStatement pstmt;
        ResultSet result;

        try{
            con = DBConnection.createConnection();
            String query = "SELECT * FROM SLOTS";
            pstmt = con.prepareStatement(query);
            result = pstmt.executeQuery();
            while (result.next()) {
                // Retrieve data from the result set for each row
                Time startTime = result.getTime("startTime");
                Time endTime = result.getTime("endTime");
                fetchedList.add(new Slot(startTime.toLocalTime(), endTime.toLocalTime()));
                // Add a new Users object to the fetchedList
            }
            // Close resources
            result.close();
            pstmt.close();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return fetchedList;
    }

//    public LocalTime[] getAllUserTime(){
//        LocalTime [] userTime = new LocalTime[usersList.size()];
//        for(int i = 0; i< usersList.size();i++){
//            userTime[i] = LocalTime.parse(usersList.get(i).getTime());
//        }
//        return  userTime;
//    }
    public static void logOut(String logOutEmail, String logOutPassword) {

        Connection con;
        PreparedStatement pstmt;
        int num;

        try{
            con = DBConnection.createConnection();
            String query = "UPDATE users SET isLoggedIn=FALSE WHERE email=?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1,logOutEmail);
            num = pstmt.executeUpdate();
            if(num>0){
                System.out.println("Logged out successfully");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        for (Users users : usersList) {
            if (logOutEmail.equals(users.getEmail()) && logOutPassword.equals(users.getPassword())) {
                users.setLoggedIn(false);

            }
        }
    }
}



