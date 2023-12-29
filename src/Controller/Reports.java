package Controller;

import Model.Appointments;
import Model.Contacts;
import Model.Customer;
import helper.AppointmentDB;
import helper.ContactDB;
import helper.CountryDB;
import helper.CustomerDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

/** Controller class for Displaying the Reports menu. */
public class Reports implements Initializable {
    public TableView<Model.Appointments> ContactTable;
    public TableColumn<Object, Object> ContactIDColumn;
    public TableColumn<Object, Object> appointmentIDColumn;
    public TableColumn<Object, Object> TitleColumn;
    public TableColumn<Object, Object> typeColumn;
    public TableColumn<Object, Object> descriptionColumn;
    public TableColumn<Object, Object> startColumn;
    public TableColumn<Object, Object> endColumn;
    public TableColumn<Object, Object> customerIDColumn;
    public ComboBox<Month> monthCombo;
    public TextField AppointmentCounterLabel;
    public ComboBox<Contacts> ContactCombo;
    public ComboBox<String> countryCombo;
    public TextField countryCounter;
    public ComboBox<String> typeCombo;

    /** This method initializes the reports menu. Sets the contact schedule and populates combo boxes. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        ContactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));


        ContactTable.setItems(AppointmentDB.getAllAppointments());
        ContactCombo.setItems(ContactDB.getAllContacts());
        countryCombo.setItems(CountryDB.getAllCountries());

        // Adds the list of available months to the months combo box
        ObservableList<Month> months = FXCollections.observableArrayList();
        months.addAll(Arrays.asList(Month.values()));
        monthCombo.setItems(months);

        ObservableList<String> typeList = FXCollections.observableArrayList();
        for(Appointments type : AppointmentDB.getAllAppointments()){
            typeList.add(type.getType());
        }
        typeCombo.setItems(typeList);
    }

    /** This method will take the user back to the main menu when done with the reports. */
    public void Home(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/mainMenu.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /** This report implements a counter when a month is selected to count how many appointments are scheduled within that month. */
    // Report 1
    public void monthSelect(ActionEvent actionEvent) {
        int counter = 0;

        if (typeCombo.getValue() != null){
            for(Appointments appt : AppointmentDB.getAllAppointments()){
                if (appt.getStartTime().getMonth().toString().equals(monthCombo.getValue().toString()) && appt.getType().toString().equals(typeCombo.getValue().toString())){
                    counter++;
                }
            }
        }
        AppointmentCounterLabel.setText(String.valueOf(counter));
    }

    /** This report implements a counter when a type is selected to count how many appointments are scheduled with that selected type. */
    public void typeSelect(ActionEvent actionEvent) {
        int counter = 0;

        if (monthCombo.getValue() != null){
            for(Appointments appt : AppointmentDB.getAllAppointments()){
                if (appt.getStartTime().getMonth().toString().equals(monthCombo.getValue().toString()) && appt.getType().toString().equals(typeCombo.getValue().toString())){
                    counter++;
                }
            }
        }
        AppointmentCounterLabel.setText(String.valueOf(counter));
    }

    /** This report allows the user to view the schedule for a selected contact.
     *  This is done by displaying all appointments connected to the selected appointment ID. */
    // Report 2
    public void ContactSelect(ActionEvent actionEvent) {
        ObservableList <Appointments> contactList = FXCollections.observableArrayList();
        for(Appointments appt : AppointmentDB.getAllAppointments()){

            if(appt.getContactId() == ContactCombo.getValue().getContactId()){
                contactList.add(appt);
            }
        }
        ContactTable.setItems(contactList);
    }

    /** This report implements a counter when a country is selected to count how many appointments are scheduled with customers in that certain country.
     * Information like this would be useful for observing demographics and making business decisions. */
    // Custom Report 3
    public void countrySelect(ActionEvent actionEvent) {
        int counter = 0;

        if (countryCombo.getValue() != null){
            for(Customer customer : CustomerDB.getAllCustomers()){
                if (customer.getCountry().equals(countryCombo.getValue())){
                    counter++;
                }
            }
        }
        countryCounter.setText(String.valueOf(counter));
    }


}
