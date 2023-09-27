
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class View{

    public static void menu1(){
        System.out.println("1.Sign Up");
        System.out.println("2.Log in");
        System.out.println("3.Exit");
    }
    public static void menu2(){
        System.out.println("1.Add Time");
        System.out.println("2.Update Time");
        System.out.println("3.View all user Time");
        System.out.println("4.Generate best time");
        System.out.println("5.Log Out");
    }

    // public static void addingUsers() {
    //     DataManager dataManager = new DataManager();
    //     for (int i = 0; i < 11; i++) {
    //         dataManager.addUser(new Users("Alek " + i, "Ramirez "+i,i,"10:1" + i));
    //     }
    // }

//    public static void addingUsers(DataManager dataManager) {
//          DataManager dataManager
//        Scanner scanner = new Scanner(System.in);
//        for(int i= 0; i<1;i++){
//            System.out.println("ADDING NEW USER");
//            System.out.println("Enter your name : ");
//            String name = scanner.nextLine();
//            System.out.println("Enter your last name : ");
//            String lastName = scanner.nextLine();
//            System.out.println("Enter your id : ");
//            int  id = scanner.nextInt();
//            scanner.nextLine();
//            System.out.println("Enter your time : ");
//            String time = scanner.nextLine();
//            System.out.println("User with id " + id+ " was added.");
//            Users newUser = new Users(name,lastName,id,time);
//            dataManager.usersList.add(newUser);
//
//        }


//        return dataManager.getUser(name, lastName);
    // type in name last name id and time


//    }

    public static void main(String[] args) {

        //Hardcoded some values for us to test
        Users user1 = new Users("Alex","Joeseph","user1@gmail.com", "user1");
        Users user2 = new Users("Zeshan","Merchant","user2@gmail.com","user2");
        Users user3 = new Users("Jackson","Jonas","user3@gmail.com","user3");
        Users user4 = new Users("Sub","Roza","user4@gmail.com","user4");
        DataManager dataManager = new DataManager();
        Scanner in = new Scanner(System.in);
        int choice1;
        int choice2;
        int userID;

        //adding user in arrayList using your method addUser
        dataManager.addUser(user1);
        dataManager.addUser(user2);
        dataManager.addUser(user3);
        dataManager.addUser(user4);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        DataManager.slotList.add(new Slot(LocalTime.parse("12:00", formatter), LocalTime.parse("14:00", formatter)));
        DataManager.slotList.add(new Slot(LocalTime.parse("14:00", formatter), LocalTime.parse("16:00", formatter)));
        DataManager.slotList.add(new Slot(LocalTime.parse("16:00", formatter), LocalTime.parse("18:00", formatter)));
        DataManager.slotList.add(new Slot(LocalTime.parse("18:00", formatter), LocalTime.parse("20:00", formatter)));
        DataManager.slotList.add(new Slot(LocalTime.parse("20:00", formatter), LocalTime.parse("22:00", formatter)));


        //Uncomment this to see that the users are added successfully.

//        for (int i = 0; i<dataManager.usersList.size();i++){
//            System.out.println(dataManager.usersList.get(i).getId());
//            System.out.println(dataManager.usersList.get(i).getName());
//            System.out.println(dataManager.usersList.get(i).getLastName());
//            System.out.println(dataManager.usersList.get(i).getTime());
//        }



        while(true){
            System.out.println("WELCOME TO THE HANGOUT PLANNER");
            menu1();
            try{
                System.out.print("Enter your choice : ");
                choice1 = in.nextInt();
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
                        in.nextLine(); // Consume the leftover newline character
                        System.out.print("Enter your email : ");
                        String email = in.nextLine();
                        System.out.print("Enter your password : ");
                        String password = in.nextLine();
                        if(DataManager.logIn(email,password)){
                            System.out.println("Logged In Successfully");
                            for(int i=0; i< DataManager.usersList.size();i++){
                                if(DataManager.usersList.get(i).getEmail().equals(email)){
                                    System.out.println("Welcome " + DataManager.usersList.get(i).getName());
                                    break;
                                }
                            }
                            boolean isLoggedIn = true;
                            while(isLoggedIn){
                                menu2();
                               try{
                                   System.out.print("Enter your choice : ");
                                   choice2 = in.nextInt();
                                   in.nextLine();
                               }catch (InputMismatchException e){
                                   System.out.println("Invalid input. Please enter a valid input");
                                   in.nextLine(); // Consume the invalid input
                                   continue;
                               }
                                //is the user is logged in, keep on showing this menu
                                switch(choice2) {
                                    case 1:
                                    boolean validTimeInput = false;
                                    LocalTime newTime = null;
                                    while (!validTimeInput) {
                                        System.out.println("Enter the time to add (HH:MM)");
                                        System.out.print("NOTE : The time should be between 12:00PM to 20:00PM : ");
                                        String timeInput = in.nextLine();
                                        try {
                                            newTime = LocalTime.parse(timeInput, formatter);
                                            validTimeInput = true; // Mark input as valid and exit the loop
                                        } catch (DateTimeParseException e) {
                                            System.out.println("Invalid time format. Please enter time in HH:MM format.");
                                        }
                                    }
                                    DataManager.addTime(email, newTime);
                                    break;
                                    case 2:
                                        DataManager.updateTime(email);
                                        break;
                                    case 3:
                                        System.out.println("Here is all user time");
                                        dataManager.displayAllTime();
                                        break;
                                    case 4:
                                        DataManager.generateBestTime(DataManager.usersList);
                                        break;
                                    case 5:
                                        System.out.println("Logging Out");
                                        System.out.println();
                                        DataManager.logOut(email,password);
                                        isLoggedIn = false;
                                        break;
                                    default:
                                        System.out.println("Invalid Input");
                                }
                            }
                            break;
                        }
                        else {
                            System.out.println("Login failed : Invalid email or password");
                            break;
                        }
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