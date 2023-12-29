package Controller;

import Model.FirstLevelDivision;
import helper.AlertPage;
import helper.CountryDB;
import helper.CustomerDB;
import helper.firstLevelDivisionDB;
import javafx.collections.ObservableList;
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

/** Controller class for Creating customers. */
public class CreateCustomer implements Initializable {


    public TextField IDfield;
    public TextField nameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneNumberField;
    public Button SaveButton;
    public Button CancelButton;
    public ComboBox<String> CountryCombo;
    public ComboBox<FirstLevelDivision> ProvinceCombo;

    private final ObservableList<String> countryList = CountryDB.getAllCountries();

    /** This method initializes the Create Customer page. Pre populates all combo boxes. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CountryCombo.setPromptText("Country");
        CountryCombo.setItems(countryList);
        ProvinceCombo.setPromptText("State/Province");

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

    /** This method takes the information from text fields and combo boxes and saves to the dedicated customer object. */
    public void Save(ActionEvent actionEvent) throws IOException {
        try{
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
                int rowsAffected = CustomerDB.createCustomer(name,address,postalCode,phoneNumber,provinceIndex);

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
        }
        catch  (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Must enter all fields");
            alert.showAndWait();
            return;

        } catch (SQLException throwable) {
            throwable.printStackTrace();
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
