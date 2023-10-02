import java.util.Scanner;

public class View {
    private Scanner in;

    public View() {
        in = new Scanner(System.in);
    }

    public int displayMenu1() {
        System.out.println("1. Sign Up");
        System.out.println("2. Log in");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        return in.nextInt();
    }

    public int displayMenu2() {
        System.out.println("1. Add Time");
        System.out.println("2. Update Time");
        System.out.println("3. View all user Time");
        System.out.println("4. Generate best time");
        System.out.println("5. Log Out");
        System.out.print("Enter your choice: ");
        return in.nextInt();
    }

    public String getEmail() {
        in.nextLine(); // Consume newline character
        System.out.print("Enter your email: ");
        return in.nextLine();
    }

    public String getPassword() {
        System.out.print("Enter your password: ");
        return in.next();
    }
}
