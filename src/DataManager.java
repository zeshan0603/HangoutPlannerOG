
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;



public class DataManager {

    private static Scanner in = new Scanner(System.in);
    public static ArrayList<Users> usersList;

    public static ArrayList<Slot> slotList = new ArrayList<Slot>();

    int [] slotsCounter = new int[slotList.size()];


    static int count = 0;

    DataManager() {
        usersList = new ArrayList<>();
    }
    public void addUser(Users newUser) {
        if(!checkSize()) {count++;
            usersList.add(newUser);}

        else System.out.println("users are full");
    }

    public void removeUser(int id) {
        for (int i = 0; i < usersList.size(); i++) {
            if(usersList.get(i).getId() == id) usersList.remove(i);
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

    public static void updateTime(String email) {
        boolean timeAdded = false; // Flag to track whether time was added
        for (Users user : usersList) {
            if (user.getEmail().equals(email) && user.getTime() != null) {
                while (!timeAdded) {
                    System.out.println("Enter the new time to add (HH:MM)");
                    System.out.print("NOTE : The time should be between 12:00PM to 20:00PM : ");
                    String updatedTimeInput = in.nextLine();
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");

                    try {
                        LocalTime updatedTime = LocalTime.parse(updatedTimeInput, formatter1);
                        user.setTime(updatedTime);
                        System.out.println("New time " + updatedTime);
                        timeAdded = true; // Set the flag to true to exit the loop
                    } catch (DateTimeParseException e) {
                        System.out.println("Please enter the time in correct format");
                    }
                }
                break; // Exit the loop once time is updated
            }
        }

        // Check if time was added or not
        if (!timeAdded) {
            System.out.println("Please add time first!");
        }
    }


    /*ADD TIME METHOD
      This method takes a user id and if that id is found in the array list, it will add the time else it will print
      user not found.
      * */
    public  static void addTime(String email, LocalTime newTime){
        for (Users users : usersList) {
            if (users.getEmail().equals(email)) {
                if(users.getTime()==null){
                    users.setTime(newTime);
                    if(users.getTime()!=null){
                        System.out.println(users.getTime());
                        System.out.println("Time has been added");
                    }
                } else{
                    System.out.println("Time has already been added, please update time");
                }
                break;
            }
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

    public static void generateBestTime(ArrayList<Users> users) {
        int userAddedTimeCount = 0;

        // Create an ArrayList to store LocalTime objects for users with added time
        ArrayList<LocalTime> userTimes = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getTime() != null) {
                userAddedTimeCount++;
                userTimes.add(LocalTime.parse(users.get(i).getTime(), DateTimeFormatter.ofPattern("HH:mm")));
            }
        }

        if (userAddedTimeCount >= 2) {
            int[] slotsCounter = new int[slotList.size()];

            for (int i = 0; i < slotList.size(); i++) {
                Slot slot = slotList.get(i);
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
            ArrayList<Slot> bestSlots = new ArrayList<>();

            // Find the maximum count
            for (int count : slotsCounter) {
                if (count > maxCount) {
                    maxCount = count;
                }
            }

            // Add slots with the maximum count to the bestSlots list
            for (int i = 0; i < slotsCounter.length; i++) {
                if (slotsCounter[i] == maxCount) {
                    bestSlots.add(slotList.get(i));
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

//    public LocalTime[] getAllUserTime(){
//        LocalTime [] userTime = new LocalTime[usersList.size()];
//        for(int i = 0; i< usersList.size();i++){
//            userTime[i] = LocalTime.parse(usersList.get(i).getTime());
//        }
//        return  userTime;
//    }
    public void displayAllTime(){
        for (Users users : usersList) {
            if (users.getTime() == null) {
                System.out.println(users.getName() + " has not added a time yet");
            } else {
                System.out.println(users.getName() + "'s time is " + users.getTime());
            }
        }
    }

    public static void signUp() {
        System.out.println("ADDING NEW USER");
        System.out.print("Enter your name : ");
        String name = in.nextLine();
        System.out.print("Enter your last name : ");
        String lastName = in.nextLine();
        System.out.print("Enter your email : ");
        String email = in.nextLine();
        System.out.print("Enter your password : ");
        String password = in.nextLine();
        System.out.print("Confirm password : ");
        String confirmPassword = in.nextLine();

        if (password.equals(confirmPassword)) {
            Users newUser = new Users(name, lastName, email, password);
            usersList.add(newUser);
            System.out.println("User with id " + newUser.getId() + " was added.");
        } else {
            System.out.println("Passwords do not match, please try again!");
        }
    }

    public static boolean logIn(String logInEmail, String logInPassword){
        for (Users users : usersList) {
            if (logInEmail.equals(users.getEmail()) && logInPassword.equals(users.getPassword())) {
                users.setLoggedIn(true);
                return true;
            }
        }
//        System.out.println("user not found");
       return false;
    }

    public static void logOut(String logOutEmail, String logOutPassword) {
        for (Users users : usersList) {
            if (logOutEmail.equals(users.getEmail()) && logOutPassword.equals(users.getPassword())) {
                users.setLoggedIn(false);

            }
        }
    }
}



