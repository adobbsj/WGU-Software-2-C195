package helper;

import Model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/** Helper class for Querying all appointments. */
public abstract class AppointmentDB {

    /** Method executes sql query to gather all pre populated appointment information and save it to the appointment list. */
    public static ObservableList<Appointments> getAllAppointments() {

        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

        try{
            String sql= "SELECT * FROM appointments";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);

            while(rs.next()){
                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");

                Appointments newAppointment = new Appointments(id, title, description, location, type, startTime, endTime, customerId, userId, contactId);
                appointmentList.add(newAppointment);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return appointmentList;
    }

    /** Method executes sql query to take the information entered by the user and store it into the database in the appointments table. */
    public static int createAppointment(String title, String description, String location, String type, LocalDateTime startTime, LocalDateTime endTime, int customerId, int userId, int contactId) throws SQLException {

        String sql = "INSERT INTO appointments VALUES(NULL,?,?,?,?,?,?,NULL,NULL,CURRENT_TIMESTAMP ,NULL,?,?,?);";

        PreparedStatement psCreate = JDBC.getConnection().prepareStatement(sql);

        psCreate.setString(1, title);
        psCreate.setString(2, description);
        psCreate.setString(3, location);
        psCreate.setString(4, type);
        psCreate.setTimestamp(5, Timestamp.valueOf(startTime));
        psCreate.setTimestamp(6, Timestamp.valueOf(endTime));
        psCreate.setInt(7, customerId);
        psCreate.setInt(8, userId);
        psCreate.setInt(9, contactId);

        int rowsAffected = psCreate.executeUpdate();
        return rowsAffected;
    }

    /** Method executes sql query to take the information entered by the user and modify the current information set for that appointment in the database. */
    public static int modifyAppointment(int Id, String title, String description, String location, String type, LocalDateTime startTime, LocalDateTime endTime, int customerId, int userId, int contactId) throws SQLException {

            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?;";

            PreparedStatement psModify = JDBC.getConnection().prepareStatement(sql);

            psModify.setString(1,title);
            psModify.setString(2,description);
            psModify.setString(3,location);
            psModify.setString(4,type);
            psModify.setTimestamp(5, Timestamp.valueOf(startTime));
            psModify.setTimestamp(6, Timestamp.valueOf(endTime));
            psModify.setInt(7,customerId);
            psModify.setInt(8,userId);
            psModify.setInt(9,contactId);
            psModify.setInt(10,Id);

            int rowsAffected = psModify.executeUpdate();
            return rowsAffected;
    }

    /** Method executes sql query to remove the selected appointment from the database. */
    public static int deleteAppointment(int Id) throws SQLException {

            String sql = "DELETE from appointments WHERE Appointment_ID = ?";

            PreparedStatement psDelete = JDBC.getConnection().prepareStatement(sql);
            psDelete.setInt(1, Id);

            int rowsAffected = psDelete.executeUpdate();
            return rowsAffected;
    }

    /** Method executes sql query to remove the associated customer appointment from the database when a customer is deleted. */
    public static int deleteCAppointment(int customerId) throws SQLException {

        String sql = "DELETE from appointments WHERE Customer_ID = ?";

        PreparedStatement psDelete = JDBC.getConnection().prepareStatement(sql);
        psDelete.setInt(1, customerId);

        int rowsAffected = psDelete.executeUpdate();
        return rowsAffected;
    }
}
