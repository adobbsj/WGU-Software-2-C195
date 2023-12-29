package helper;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Helper class for Querying all customers. */
public abstract class CustomerDB {

    /** Method executes sql query to gather all pre populated customer information and save it to the customer list. */
    public static ObservableList<Customer> getAllCustomers() {

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try{

            String sql =
                    """
                            SELECT Customer_ID, Customer_Name, countries.Country, first_level_divisions.Division, Address, Postal_Code, Phone
                            FROM customers, first_level_divisions, countries
                            WHERE customers.Division_ID = first_level_divisions.Division_ID AND first_level_divisions.COUNTRY_ID = countries.Country_ID""";

            PreparedStatement PS = JDBC.getConnection().prepareStatement(sql);
            ResultSet RS = PS.executeQuery();

            while(RS.next()){

                int customerId = RS.getInt("Customer_ID");
                String customerName = RS.getString("Customer_Name");
                String customerAddress = RS.getString("Address");
                String postalCode = RS.getString("Postal_Code");
                String phoneNumber = RS.getString("Phone");
                String Division = RS.getString("Division");
                String Country = RS.getString("Country");



                Customer newCustomer = new Customer(customerId, customerName, customerAddress, postalCode, phoneNumber, Division, Country);
                customerList.add(newCustomer);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return customerList;
    }

    /** Method executes sql query to take the information entered by the user and store it into the database in the customer table. */
     public static int createCustomer(String customerName, String customerAddress, String postalCode, String phoneNumber, int  Division) throws SQLException {
            String sqlCreate = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?,?,?,?,?)";
            PreparedStatement psCreate = JDBC.getConnection().prepareStatement(sqlCreate);

         psCreate.setString(1,customerName);
         psCreate.setString(2,customerAddress);
         psCreate.setString(3,postalCode);
         psCreate.setString(4,phoneNumber);
         psCreate.setInt(5,Division);

         int rowsAffected = psCreate.executeUpdate();
         return rowsAffected;
    }

    /** Method executes sql query to take the information entered by the user and modify the current information set for that customer in the database. */
    public static int modifyCustomer(int customerId,String customerName, String customerAddress, String postalCode, String phoneNumber, int Division) throws SQLException {

            String sqlModify = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?;";

            PreparedStatement psModify = JDBC.getConnection().prepareStatement(sqlModify);

            psModify.setString(1, customerName);
            psModify.setString(2, customerAddress);
            psModify.setString(3, postalCode);
            psModify.setString(4, phoneNumber);
            psModify.setInt(5, Division);
            psModify.setInt(6, customerId);

            int rowsAffected = psModify.executeUpdate();
            return rowsAffected;


    }

    /** Method executes sql query to remove the selected customer from the database. */
    public static int deleteCustomer(int customerId) throws SQLException {

            String sqlDelete = "DELETE from customers WHERE Customer_ID = ?";

            PreparedStatement psDelete = JDBC.getConnection().prepareStatement(sqlDelete);

            psDelete.setInt(1, customerId);

            int rowsAffected = psDelete.executeUpdate();
            return rowsAffected;
        }
}

