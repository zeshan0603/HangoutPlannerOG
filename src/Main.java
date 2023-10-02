
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {



    public static void main(String[] args) {

        //Hardcoded some values for us to test
//        Users user1 = new Users("Alex","Joeseph","user1@gmail.com", "user1");
//        Users user2 = new Users("Zeshan","Merchant","user2@gmail.com","user2");
//        Users user3 = new Users("Jackson","Jonas","user3@gmail.com","user3");
//        Users user4 = new Users("Sub","Roza","user4@gmail.com","user4");
        View view = new View();
        DataManager dataManager = new DataManager();
        Scanner in = new Scanner(System.in);
        int choice1;
        int choice2;
        int userID;

        //adding user in arrayList using your method addUser
//        dataManager.addUser(user1);
//        dataManager.addUser(user2);
//        dataManager.addUser(user3);
//        dataManager.addUser(user4);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        DataManager.slotDataList.add(new Slot(LocalTime.parse("12:00", formatter), LocalTime.parse("14:00", formatter)));
        DataManager.slotDataList.add(new Slot(LocalTime.parse("14:00", formatter), LocalTime.parse("16:00", formatter)));
        DataManager.slotDataList.add(new Slot(LocalTime.parse("16:00", formatter), LocalTime.parse("18:00", formatter)));
        DataManager.slotDataList.add(new Slot(LocalTime.parse("18:00", formatter), LocalTime.parse("20:00", formatter)));
        DataManager.slotDataList.add(new Slot(LocalTime.parse("20:00", formatter), LocalTime.parse("22:00", formatter)));


//        DataManager.fetchUserData();

        while(true){
            System.out.println("WELCOME TO THE HANGOUT PLANNER");

            try{
                choice1 =   view.displayMenu1();
            }catch (InputMismatchException e){
                System.out.println("Please enter a valid input");
                in.nextLine();
                continue;
            }
            switch (choice1){
                case 1 :
                    System.out.println("Sign up");
                    DataManager.signUp();
                    break;
                case 2 :
                    while (true){
                        System.out.println("Logging In");
                        String email = view.getEmail();
                        String password = view.getPassword();
                        if(DataManager.logIn(email,password)){
                            boolean isLoggedIn = true;
                            while(isLoggedIn){
                               try
                               {
                                   choice2 = view.displayMenu2();
                               }
                               catch (InputMismatchException e)
                               {
                                   System.out.println("Invalid input. Please enter a valid input");
                                   in.nextLine(); // Consume the invalid input
                                   continue;
                               }
                                //is the user is logged in, keep on showing this menu
                                switch (choice2) {
                                    case 1 -> {
                                        boolean validTimeInput = false;
                                        LocalTime newTime = null;
                                        while (!validTimeInput) {
                                            System.out.println("Enter the time to add (HH:MM)");
                                            System.out.print("NOTE : The time should be between 12:00PM to 20:00PM : ");
                                            try {
                                                newTime = DataManager.takeTimeInput();
                                                validTimeInput = true; // Mark input as valid and exit the loop
                                            } catch (DateTimeParseException e) {
                                                System.out.println("Invalid time format. Please enter time in HH:MM format.");
                                            }
                                        }
                                        DataManager.addTime(email, newTime);
                                    }
                                    case 2 -> DataManager.updateTime(email);
                                    case 3 -> {
                                        System.out.println("Here is all user time");
                                        dataManager.displayAllTime();
                                    }
                                    case 4 -> DataManager.generateBestTime();
                                    case 5 -> {
                                        System.out.println("Logging Out");
                                        System.out.println();
                                        DataManager.logOut(email, password);
                                        isLoggedIn = false;
                                    }
                                    default -> System.out.println("Invalid Input");
                                }
                            }
                        }
                        else {
                            System.out.println("Login failed : Invalid email or password");
                        }
                        break;
                    }
                    break;
                case 3 :
                    System.out.println("THANK U");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}