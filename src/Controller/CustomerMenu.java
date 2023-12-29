package Controller;

import Model.Appointments;
import Model.Customer;
import helper.AppointmentDB;
import helper.CustomerDB;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class for Displaying the customer menu. */
public class CustomerMenu implements Initializable {

    public TableView<Model.Customer> CustomerTable;
    public TableColumn<Object, Object> CustomerIDColumn;
    public TableColumn<Object, Object> NameColumn;
    public TableColumn<Object, Object> AddressColumn;
    public TableColumn<Object, Object> PostalCodeColumn;
    public TableColumn<Object, Object> PhoneNumberColumn;
    public RadioButton WeekRadio;
    public RadioButton MonthRadio;
    public RadioButton AllRadio;
    public RadioButton CustomerRadio;
    public TableColumn<Object, Object> CountryColumn;

    /** This method initializes the Customer Menu. populates customer table and sets radio button to true. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustomerRadio.setSelected(true);

        CustomerTable.setItems(CustomerDB.getAllCustomers());

        CustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        AddressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        PostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        PhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        CountryColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));


    }

    /** This method takes the user to the Create customer menu. */
    public void addCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/createCustomer.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /** This method takes the user to the modify customer menu. Will also send all customer information to the menu as well. */
    public void modifyCustomer(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyCustomer.fxml"));
            loader.load();
            ModifyCustomer modifyCustomer = loader.getController();
            Customer sC = CustomerTable.getSelectionModel().getSelectedItem();
            modifyCustomer.receiveCustomer(sC);

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Must select customer to modify");
            alert.showAndWait();
        }
    }

    /** This method takes the selected customer and removes them from the database. Will also remove associated appointments connected to that customer. */
    public void removeCustomer(ActionEvent actionEvent) throws SQLException {

    try {
        int customerId = CustomerTable.getSelectionModel().getSelectedItem().getCustomerId();
            Customer sC = CustomerTable.getSelectionModel().getSelectedItem();
    if (sC != null) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("DELETE Customer");
        alert.setContentText("Are you sure you want to delete selected customer? All scheduled appointments with this customer will be removed as well");
        Optional<ButtonType> choice = alert.showAndWait();

        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            for (Appointments appt : AppointmentDB.getAllAppointments()){
                if (customerId == appt.getCustomerId()){
                    AppointmentDB.deleteCAppointment(appt.getCustomerId());
                    CustomerDB.deleteCustomer(customerId);
                }

            CustomerTable.setItems(CustomerDB.getAllCustomers());
        }
            // deletes a customer if there is no associated appointment
            CustomerDB.deleteCustomer(customerId);
            CustomerTable.setItems(CustomerDB.getAllCustomers());

            // lets user know that the connected appointment has been removed
            Alert alertOne = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Customer removed");
            alert.setContentText(sC.getCustomerName() + " has been removed along with all scheduled appointments");
            Optional<ButtonType> choiceOne = alert.showAndWait();


        }

    }

    } catch (NullPointerException n)
        {
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setContentText("select customer to remove");
        alertError.showAndWait();
    }
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

    /** This method when clicked will display all of the appointments on the appointments page. */
    public void allView(ActionEvent actionEvent) throws IOException {
        AllRadio.setSelected(true);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/mainMenu.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /** This method when clicked will display all of the customers and relevant information on the customer page. */
    public void CustomerView(ActionEvent actionEvent) {
        CustomerRadio.setSelected(true);
        WeekRadio.setSelected(false);
        AllRadio.setSelected(false);
        MonthRadio.setSelected(false);
    }


}
