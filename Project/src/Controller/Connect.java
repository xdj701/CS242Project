package Controller;
import java.sql.*;

/**
 * Created by dajun on 4/30/17.
 */
public class Connect {

    private Connection connection;

    //jdbc library loaded
    public Connect() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/splendor","root","1234");

    }

    //disconnect from database
    public void disconnect() throws SQLException {
        connection.close();
    }

    //execute query to validate if the username matches with the password
    public boolean verify(String usr, String pin) throws SQLException {

        Statement stmt= connection.createStatement();
        ResultSet rs=stmt.executeQuery("select password from users where username = '" + usr +"'");
        if(rs.first()){
            String password = rs.getString(1);
            if(password.equals(pin)) {
                return true;
            }
        }

        return false;

    }


}


