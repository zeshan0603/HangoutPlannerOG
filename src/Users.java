import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Users {

    //private variables is a good practice
    private String name;
    private String time;
    private String lastName;
    private int id ;

    private static int userCount=0;
    private String email;

    private String password;

    private  boolean isSignedUp = false;

    private boolean isLoggedIn = false;

    //Removed time parameter from constructor as we will set time using setter and addTime method.

    public Users(String name, String lastName, String email, String password, LocalTime time) {
        userCount++;
        this.setName(name);
        this.setLastName(lastName);
        this.id = userCount;
        this.setEmail(email);
        this.setPassword(password);
        this.isSignedUp = true;
        this.setTime(time);
    }

    //NAME
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName(String email){
        if(this.getEmail().equals(email)){
            return this.getName();
        }
        return "null";
    }

    //ID
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    //TIME
//    public void setTime(LocalTime time) {
//        this.time = time;
//    }
    public String getTime() {
        return time;
    }

    //EMAIL AND PASSWORD
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSignedUp() {
        return isSignedUp;
    }

    public void setSignedUp(boolean signedUp) {
        isSignedUp = signedUp;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }


    public void setTime(LocalTime localTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        // Define the allowed time range
        LocalTime startTime = LocalTime.of(12, 0); // 12:00 PM
        LocalTime endTime = LocalTime.of(20, 0);   // 8:00 PM

        if (localTime != null) {
            // Check if the provided time is within the allowed range
            if (!localTime.isBefore(startTime) && !localTime.isAfter(endTime)) {
                // Convert LocalTime to String in the desired format, e.g., "HH:mm"
                this.time = String.valueOf(LocalTime.parse(localTime.format(formatter)));
            }
        }
    }








}