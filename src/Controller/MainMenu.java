package Controller;

import Model.Appointments;
import helper.AppointmentDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class for Displaying the main appointment menu. */
public class MainMenu implements Initializable {
    public TableView<Appointments> AppointmentTable;
    public TableColumn<Appointments, Integer> AppointmentIDColumn;
    public TableColumn<Appointments, String> TitleColumn;
    public TableColumn<Appointments, String> DescriptionColumn;
    public TableColumn<Appointments, String> LocationColumn;
    public TableColumn<Appointments, String> TypeColumn;
    public TableColumn<Appointments, LocalDateTime> StartTimeColumn;
    public TableColumn<Appointments, LocalDateTime> EndTimeColumn;
    public TableColumn<Appointments, Integer> UserIDColumn;
    public TableColumn<Appointments, Integer> CustomerIDColumn;
    public TableColumn<Appointments, Integer> ContactIDColumn;
    public RadioButton WeekRadio;
    public RadioButton MonthRadio;
    public RadioButton customerViewRadio;
    public RadioButton AllRadio;
    public TextField LocationLabel;
    public TextField TimeLabel;

    /** This method initializes the main Menu. populates appointment table and sets radio button to true. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        TimeLabel.setText(dtf.format(now));

        LocationLabel.setText(ZoneId.systemDefault().getId());
        AllRadio.setSelected(true);
       AppointmentTable.setItems(AppointmentDB.getAllAppointments());

        AppointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        StartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        EndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        UserIDColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        ContactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
    }

    /** This method when clicked will display all of the customers and relevant information on the customer page. */
    public void customerClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/customerMenu.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        customerViewRadio.setSelected(true);
    }

    /** This method takes the user to the Create appointment menu. */
    public void createApt(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/View/createAppointment.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /** This method takes the user to the modify appointment menu. Will also send all appointment information to the menu as well. */
    public void changeApt(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/changeAppointment.fxml"));
            loader.load();
            ChangeAppointment changeAppointment = loader.getController();
            Appointments selectAppointment = AppointmentTable.getSelectionModel().getSelectedItem();
            changeAppointment.receiveAppointment(selectAppointment);

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Must select appointment to modify");
            alert.showAndWait();
        }
    }

    /** This method takes the selected appointment and removes it from the database. */
    public void removeApt(ActionEvent actionEvent) throws SQLException {
        int appointmentId = AppointmentTable.getSelectionModel().getSelectedItem().getId();
        Appointments selectAppointment = AppointmentTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Appointment");
        alert.setContentText("Are you sure you want to remove the selected appointment?");
        Optional<ButtonType> choice = alert.showAndWait();

        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            int rowsAffected = AppointmentDB.deleteAppointment(appointmentId);
            AppointmentTable.setItems(AppointmentDB.getAllAppointments());

            Alert alertOne = new Alert(Alert.AlertType.CONFIRMATION);
            alertOne.setTitle("Appointment removed");
            alertOne.setContentText("Appointment ID: " + appointmentId + "\nOf type: " + selectAppointment.getType() + "\nhas been cancelled.");
            Optional<ButtonType> choiceOne = alertOne.showAndWait();
    }
    }

    /** This method takes the user to the reports menu. */
    public void Reports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/reports.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /** This method returns the user to the login screen upon confirmation. */
    public void logout(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cancel?");
        alert.setContentText("Are you sure you want to log off?");
        Optional<ButtonType> choice = alert.showAndWait();

        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/loginView.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    }

    /** This method when clicked will display all of the appointments and relevant information filtered by 7 days from the current day. */
    public void weekView(ActionEvent actionEvent) {
        AllRadio.setSelected(false);
        WeekRadio.setSelected(true);
        MonthRadio.setSelected(false);
        customerViewRadio.setSelected(false);

        // displays weekview by showing all appointments within 7 days of the current date
       ObservableList<Appointments> weekView = FXCollections.observableArrayList();
        for(Appointments appt : AppointmentDB.getAllAppointments()){
            if((appt.getStartTime().getDayOfYear() < (LocalDate.now().getDayOfYear() + 7)) && (appt.getStartTime().getDayOfYear() >= (LocalDate.now().getDayOfYear()))){
                weekView.add(appt);
            }

        }
        AppointmentTable.setItems(weekView);
    }

    /** This method when clicked will display all of the appointments and relevant information filtered by current month. */
    public void monthView(ActionEvent actionEvent) {
        AllRadio.setSelected(false);
        WeekRadio.setSelected(false);
        MonthRadio.setSelected(true);
        customerViewRadio.setSelected(false);

        // displays monthview by showing all appointments in the current month
        ObservableList<Appointments> monthView = FXCollections.observableArrayList();
        for(Appointments appt : AppointmentDB.getAllAppointments()){

            if(appt.getStartTime().getMonth() == LocalDate.now().getMonth()){
                monthView.add(appt);
            }

        }
        AppointmentTable.setItems(monthView);
}

    /** This method when clicked will display all of the appointments on the appointments page. */
    public void allView(ActionEvent actionEvent) {

        AllRadio.setSelected(true);
        WeekRadio.setSelected(false);
        MonthRadio.setSelected(false);
        customerViewRadio.setSelected(false);

        AppointmentTable.setItems(AppointmentDB.getAllAppointments());
    }
}
