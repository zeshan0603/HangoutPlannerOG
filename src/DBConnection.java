import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    static Connection con;

    public static Connection createConnection(){
        try{
            String url = "jdbc:mysql://localhost:3306/hangoutplanner?useSSL=false";
            String password = "root";
            String username = "root";
            con = DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
