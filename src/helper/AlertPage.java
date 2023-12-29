package helper;

import javafx.scene.control.Alert;

/** Helper class for displaying different alert messages.
 * wanted to try a new layout with functions and function calls but didn't get much onto it. */
public class AlertPage {

    /** Method returns an error message when there is missing information in any fields. */
    public static void blankField(){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setContentText("One or more fields missing information");
        error.showAndWait();
        return;
    }

    /** Method returns an error message when the postal code text field has too many characters. Must be 5 or less. */
    public static void postalWarning(){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Error");
        error.setContentText("Postal code can only be up to 5 characters");
        error.showAndWait();
        return;
    }

}




