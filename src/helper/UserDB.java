package helper;

import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Helper class for Querying all users. */
public class UserDB {

/** Method executes sql query to gather all pre populated user information and save it to the user list.
 * future references to users in the application are gathered from this list. */
    public static ObservableList<User> getAllUsers() {
        ObservableList<User> userList = FXCollections.observableArrayList();

        try {

            String sql = "SELECT User_ID, User_Name FROM users;";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                int usrId = rs.getInt("User_ID");
                String usrName = rs.getString("User_Name");

                User u = new User(usrId,usrName);
                userList.add(u);

            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    /** Method executes sql query to gather all pre populated user information (particularly the password) and save it to the user list.
     * This method is only used for logging in purposes. */
    public static ObservableList<User> getPUsers(){
        ObservableList<User> secureUsers = FXCollections.observableArrayList();

        try {

            String sql = "SELECT User_ID,User_Name,Password FROM users;";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                int userId = rs.getInt("User_ID");
                String usrName = rs.getString("User_Name");
                String usrPassword = rs.getString("Password");

                User user = new User(userId,usrName,usrPassword);
                secureUsers.add(user);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return secureUsers;
    }
}
