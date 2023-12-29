package Controller;

import Model.Customer;
import Model.FirstLevelDivision;
import helper.AlertPage;
import helper.CountryDB;
import helper.CustomerDB;
import helper.firstLevelDivisionDB;
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
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller class for Modifying customers. */
public class ModifyCustomer implements Initializable {
    public TextField IDfield;
    public TextField nameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneNumberField;
    public Button SaveButton;
    public Button CancelButton;
    public ComboBox<String> CountryCombo;
    public ComboBox<Model.FirstLevelDivision> ProvinceCombo;

    /** This method initializes the Modify Customer page. Pre populates all combo boxes. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CountryCombo.setItems(CountryDB.getAllCountries());
    }

    /** This method populates the province combo box based off of the country selection. */
    public void ProvinceSelect(ActionEvent actionEvent) {
        int countryId;

        if(CountryCombo.getSelectionModel().getSelectedIndex() == 0){
            countryId = 1;
            ProvinceCombo.setItems(firstLevelDivisionDB.getAllDivisions(countryId));
        }

        if(CountryCombo.getSelectionModel().getSelectedIndex() == 1){
            countryId = 2;
            ProvinceCombo.setItems(firstLevelDivisionDB.getAllDivisions(countryId));
        }

        if(CountryCombo.getSelectionModel().getSelectedIndex() == 2){
            countryId = 3;
            ProvinceCombo.setItems(firstLevelDivisionDB.getAllDivisions(countryId));
        }
    }

    /** This method receives all customer data and populates relevant text fields. */
    public void receiveCustomer(Customer customer) {
        int countryId = 0;

        IDfield.setText(String.valueOf(customer.getCustomerId()));
        nameField.setText(customer.getCustomerName());
        addressField.setText(String.valueOf(customer.getCustomerAddress()));
        postalCodeField.setText(String.valueOf(customer.getPostalCode()));
        phoneNumberField.setText(String.valueOf(customer.getPhoneNumber()));
        CountryCombo.setValue(customer.getCountry());
        ProvinceCombo.setItems(firstLevelDivisionDB.getAllDivisions(customer.getCustomerId()));

        //sets state/province combo box based off of the customer country.
        if(customer.getCountry().equals("U.S")) {
            countryId = 1;
        }
        if(customer.getCountry().equals("UK")){
            countryId = 2;
        }
        if(customer.getCountry().equals("Canada")){
            countryId = 3;
        }

        ProvinceCombo.setItems(firstLevelDivisionDB.getAllDivisions(countryId));

        for(FirstLevelDivision FLD : firstLevelDivisionDB.getAllDivisions(countryId)){
            if(FLD.getDivision().equals(customer.getDivision())){
                ProvinceCombo.setValue(FLD);
                break;
            }
        }
    }

    /** This method takes the information from text fields and combo boxes and saves to the dedicated customer object. */
    public void Save(ActionEvent actionEvent) {
        try{
            int customerId = Integer.parseInt(IDfield.getText());
            String name = nameField.getText();
            String address = addressField.getText();
            String postalCode = postalCodeField.getText();
            String phoneNumber = phoneNumberField.getText();
            String countryIndex = CountryCombo.getValue();
            int provinceIndex = ProvinceCombo.getValue().getDivisionId();

            // Alerts if any of the fields are empty.
            if( name.isBlank() || address.isBlank() || postalCode.isBlank() || phoneNumber.isBlank() || countryIndex.isBlank() || provinceIndex == 0){
                AlertPage.blankField();
                return;
            }
            // Alerts if postal code is longer than 5 characters.
            if (postalCode.length() > 5) {
                AlertPage.postalWarning();
                return;
            }

            else {
                int rowsAffected = CustomerDB.modifyCustomer(customerId,name,address,postalCode,phoneNumber,provinceIndex);

                if (rowsAffected > 0){
                    System.out.println("Insert Successful");
                }
                else{
                    System.out.println("Insert Failed");
                }
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/customerMenu.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    /** This method returns the user back to the customer menu without modifying any customer data. */
    public void Cancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cancel?");
        alert.setContentText("Are you sure you want to cancel? Unsaved progress will be lost.");
        Optional<ButtonType> choice = alert.showAndWait();

        if (choice.isPresent() && choice.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/customerMenu.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
