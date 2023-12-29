package Main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Main class for initializing the application. */
public class Main extends Application {

    /** Method loads the user into the login page. */
    @Override
    public void start(Stage primaryStage) throws Exception{

        // used separate view pages to initialize ans skip login.
        //Parent root = FXMLLoader.load(getClass().getResource("../View/createCustomer.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("../View/mainMenu.fxml"));

        Parent root = FXMLLoader.load(getClass().getResource("../View/loginView.fxml"));
        primaryStage.setTitle("Appointment Login");
        primaryStage.setScene(new Scene(root, 300, 350));
        primaryStage.show();
    }

    /** Method starts the program. */
    public static void main(String[] args) {

        // open and close connection called to connect to database.
        JDBC.openConnection();

        launch(args);

        JDBC.closeConnection();
    }
}
