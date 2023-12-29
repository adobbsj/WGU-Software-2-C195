package helper;

import Model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Helper class for Querying all contacts. */
public class ContactDB {

    /** Method executes sql query to gather all pre populated contact information and save it to the contact list.
     * this is all that is needed as the user does not need to add/modify/delete contacts. */
    public static ObservableList<Contacts> getAllContacts() {
        ObservableList<Contacts> contactList = FXCollections.observableArrayList();

        try {

            String sql = "SELECT Contact_ID, Contact_Name FROM contacts;";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");

                Contacts contact = new Contacts(contactId, contactName);
                contactList.add(contact);
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }
}
