package Controller;

import Model.Appointments;
import Model.Contacts;
import Model.Customer;
import Model.User;
import helper.AppointmentDB;
import helper.ContactDB;
import helper.CustomerDB;
import helper.UserDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class for Modifying appointments. */
public class ChangeAppointment implements Initializable {

    public TextField titleField;
    public TextField idField;
    public TextField descriptionField;
    public TextField locationField;
    public TextField typeField;
    public ComboBox<java.time.LocalTime> startCombo;
    public ComboBox<java.time.LocalTime> endCombo;
    public ComboBox<Model.User> userIDCombo;
    public ComboBox<Model.Customer> CustomerIDCombo;
    public ComboBox<Model.Contacts> contactIDCombo;
    public DatePicker Date;

    /** This method initializes the modify Appointment page. Pre populates  all combo boxes. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startCombo.setItems(Appointments.getTimes());
        endCombo.setItems(Appointments.getTimes());
        userIDCombo.setItems(UserDB.getAllUsers());
        CustomerIDCombo.setItems(CustomerDB.getAllCustomers());
        contactIDCombo.setItems(ContactDB.getAllContacts());
    }

    /** This method receives all appointment data and populates relevant text fields. */
    public void receiveAppointment(Appointments Appointments) {

        idField.setText((String.valueOf(Appointments.getId())));
        titleField.setText(Appointments.getTitle());
        descriptionField.setText(Appointments.getDescription());
        locationField.setText(Appointments.getLocation());
        typeField.setText(Appointments.getType());
        Date.setValue(Appointments.getStartTime().toLocalDate());
        startCombo.setValue(Appointments.getStartTime().toLocalTime());
        endCombo.setValue(Appointments.getEndTime().toLocalTime());

        for (User user : UserDB.getAllUsers()) {
            if(user.getId() == Appointments.getUserId()){
                userIDCombo.setValue(user);
                break;
            }
        }
        for (Customer customer : CustomerDB.getAllCustomers()) {
            if(customer.getCustomerId() == Appointments.getCustomerId()){
                CustomerIDCombo.setValue(customer);
                break;
            }
        }
        for (Contacts contact : ContactDB.getAllContacts()) {
            if(contact.getContactId() == Appointments.getContactId()){
                contactIDCombo.setValue(contact);
                break;
            }
        }
    }

    /** This method takes the information from text fields and saves to the dedicated appointment object. */
    public void Save(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String description = descriptionField.getText();
            String location = locationField.getText();
            String type = typeField.getText();
            LocalDate date = Date.getValue();
            LocalDateTime startTime = null;
            LocalDateTime endTime = null;
            startTime = LocalDateTime.of(Date.getValue(), startCombo.getValue());
            endTime = LocalDateTime.of(Date.getValue(), endCombo.getValue());
            int userId = userIDCombo.getValue().getId();
            int customerId = CustomerIDCombo.getValue().getCustomerId();
            int contactId = contactIDCombo.getValue().getContactId();

            // conversion to eastern time zone.
            ZoneId thisZone =ZoneId.systemDefault();
            ZonedDateTime thisStartZoneTime = startTime.atZone(thisZone);
            ZonedDateTime thisEndZoneTime = endTime.atZone(thisZone);
            ZonedDateTime eastZoneTime = thisStartZoneTime.withZoneSameInstant(ZoneId.of("America/New_York"));
            ZonedDateTime eastEndZoneTime = thisEndZoneTime.withZoneSameInstant(ZoneId.of("America/New_York"));


            //loop which checks to see if the customer has made an appointment in the same time slot already.
            for (Appointments appointment : AppointmentDB.getAllAppointments()){
                if (appointment.getCustomerId() == customerId && appointment.getId() != id)
                {
                    if(startCombo.getValue().isBefore(LocalTime.now()) && Date.getValue().isEqual(LocalDate.now())){
                        Alert alertError = new Alert(Alert.AlertType.ERROR);
                        alertError.setContentText("Cannot select a time before now");
                        alertError.showAndWait();
                        return;
                    }
                    if ((startTime.isBefore(appointment.getStartTime()) || startTime.isEqual(appointment.getStartTime())) && (endTime.isAfter(appointment.getEndTime())) || endTime.isEqual(appointment.getEndTime())) {

                        Alert alertError = new Alert(Alert.AlertType.ERROR);
                        alertError.setContentText("This time has already been reserved");
                        alertError.showAndWait();
                        return;
                    }
                    if ((endTime.isBefore(appointment.getEndTime()) || endTime.isEqual(appointment.getEndTime())) && endTime.isAfter(appointment.getStartTime())) {

                        Alert alertError = new Alert(Alert.AlertType.ERROR);
                        alertError.setContentText("This time has already been reserved");
                        alertError.showAndWait();
                        return;
                    }
                    if ((startTime.isAfter(appointment.getStartTime()) || startTime.isEqual(appointment.getStartTime())) && startTime.isBefore(appointment.getEndTime())) {

                        Alert alertError = new Alert(Alert.AlertType.ERROR);
                        alertError.setContentText("This time has already been reserved");
                        alertError.showAndWait();
                        return;
                    }
                }
            }

            // Error checking for scheduling on weekends.
            if (Date.getValue().getDayOfWeek() == DayOfWeek.valueOf("SATURDAY") || Date.getValue().getDayOfWeek() == DayOfWeek.valueOf("SUNDAY")){
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setContentText("Must schedule within business hours (8am-10pm EST Monday-Friday)");
                alertError.showAndWait();
                return;
            }
            // Error checking for scheduling outside of business hours on weekdays.
            if(eastZoneTime.isBefore(ZonedDateTime.parse(Date.getValue() + "T08:00:00.-05:00[America/New_York]")) || eastEndZoneTime.isAfter(ZonedDateTime.parse(Date.getValue() + "T22:00:00.-05:00[America/New_York]"))){
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setContentText("Must schedule within business hours (8am-10pm EST Monday-Friday)");
                alertError.showAndWait();
                return;
            }
            // Error checking for Chronological times.
            if (startCombo.getValue().isAfter(endCombo.getValue()) || startCombo.getValue().equals(endCombo.getValue())) {
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setContentText("Start time must be before End time");
                alertError.showAndWait();
                return;
            }
            // Error checking to make sure the selected date is on or after the current date.
            if (Date.getValue().isBefore(LocalDate.now())) {
                Alert alertError = new Alert(Alert.AlertType.ERROR);
                alertError.setContentText("Cannot select a time before today");
                alertError.showAndWait();
                return;
            }
            else{
                AppointmentDB.modifyAppointment(id,title,description,location,type,startTime,endTime,customerId,userId,contactId);
            }

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/MainMenu.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** This method returns the user back to the main menu without modifying any appointment data. */
    public void Cancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cancel?");
        alert.setContentText("Are you sure you want to cancel? Unsaved progress will be lost.");
        Optional<ButtonType> choice = alert.showAndWait();

        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/MainMenu.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
