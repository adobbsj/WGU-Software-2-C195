package Controller;

import Model.Appointments;
import Model.User;
import helper.AppointmentDB;
import helper.UserDB;
import interfaces.meetingInterface;
import interfaces.zoneInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

/** Controller class for Displaying the Login menu. */
public class loginController implements Initializable {
    public TextField UsernameField;
    public TextField PasswordField;
    public Label locationText;
    public Label userNameLabel;
    public Label passwordLabel;
    public Button exitButton;
    public Button loginButton;
    public Label locationLabel;
    public Label PlannerLabel;

    /** This method initializes the Login menu. Sets the text language based on user default.
     * Lambda expression 1 is Implemented by setting the users location to the location text via the interface.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (Locale.getDefault().getLanguage().equals("fr")) {
            
            ResourceBundle rb = ResourceBundle.getBundle("Main/Nat", Locale.getDefault());
            PlannerLabel.setText(rb.getString("AppointmentPlanner"));
            userNameLabel.setText(rb.getString("Username"));
            passwordLabel.setText(rb.getString("Password"));
            loginButton.setText(rb.getString("Login"));
            exitButton.setText(rb.getString("Cancel"));
            locationLabel.setText(rb.getString("Location"));
        }

        // Lambda expression 1. Implements the interface by setting the users location to the location text.
        zoneInterface zone = thisZone -> {
            return thisZone.toString();
        };

        locationText.setText(zone.getZone(ZoneId.systemDefault()));
    }

    /** This method takes the user to the main menu if both username and password are correct. */
    public void loginClick(ActionEvent actionEvent) throws IOException {
     String userName = UsernameField.getText();
     String passWord = PasswordField.getText();


     // loops through users to see if the credentials match one of them
     for (User user : UserDB.getPUsers()) {
         if((user.getUserName().equals(userName) & user.getPassword().equals(passWord)) && !UsernameField.getText().isEmpty() && !PasswordField.getText().isEmpty()) {
             Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/mainMenu.fxml")));
             Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
             Scene scene = new Scene(root);
             stage.setScene(scene);
             stage.show();

             appointmentAlert(user.getId());
             loginActivity(userName,true);
             return;
         }

     }

        ResourceBundle rb = ResourceBundle.getBundle("Main/Nat", Locale.getDefault());

     // language handler for french case
     if (Locale.getDefault().getLanguage().equals("fr")) {
         Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setTitle(rb.getString("error"));
         alert.setHeaderText(rb.getString("error"));
         alert.setContentText(rb.getString("Error"));
         alert.showAndWait();
         loginActivity(userName,false);
     }
        // language handler for english case
        if (Locale.getDefault().getLanguage().equals("en")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("error"));
            alert.setHeaderText(rb.getString("error"));
            alert.setContentText(rb.getString("Error"));
            alert.showAndWait();
            loginActivity(userName,false);
        }


    }

    /** This method reminds the logged in user if there is an appointment in 15 minutes or less.
     * Lambda expression 2 is Implemented to remind the user of an appointment within 15 minutes via the reminder interface. */
    public void appointmentAlert(int userId){
        int Counter = 0;
        String AptId = "", Date = "", Time = "";

        for (Appointments appointment : AppointmentDB.getAllAppointments()){

            if (userId == appointment.getUserId()) {
                LocalDateTime start = appointment.getStartTime();
                LocalDateTime now = LocalDateTime.now();
                long split = ChronoUnit.MINUTES.between(now, start);


            if (0 <= split && split <= 15){
                Counter++;
                AptId = Integer.toString(appointment.getId());
                Date = appointment.getStartTime().toLocalDate().toString();
                Time = appointment.getStartTime().toLocalTime().toString();
            }
            }
        }

        if (Counter > 0){

            // Lambda expression 2. Implements the appointment reminder if there is an appointment within 15 minutes.
            meetingInterface reminder = () ->{
                String Reminder = "Less than 15 minutes until next appointment";
                return Reminder;
            };

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Meeting soon");
            alert.setContentText(reminder.getReminder() + "\nAppointment ID: " + AptId + "\nTime: " + Time);
            alert.showAndWait();
        }

        // If there is no appointment within 15 minutes.
       else if (Counter == 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No upcoming meeting");
            alert.setContentText("No upcoming appointments soon");
            alert.showAndWait();
        }
    }

    /** This method keeps track of all login attempts and writes that information to a .txt file. */
    public void loginActivity (String userName, boolean loginCount) throws IOException {
        // file writer to store the login activity
        String fileName = "login_activity.txt";
        Scanner keyboard = new Scanner(System.in);
        FileWriter fileWriter = new FileWriter(fileName, true);
        PrintWriter outputFile = new PrintWriter(fileWriter);

        // formatter to make the times easier to read.
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if (loginCount) {
            outputFile.println(userName + " " + dtf.format(now) + " valid login");
        }

        else{
            outputFile.println(userName + " "  + dtf.format(now) + " invalid login");
        }
        outputFile.close();

        System.out.println("File has been written");
    }

    /** This method when clicked will close the entire application. */
    public void exitClick(ActionEvent actionEvent) {
        Platform.exit();
    }
}
