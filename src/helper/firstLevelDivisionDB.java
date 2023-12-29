package helper;

import Model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Helper class for Querying all First level divisions. */
public class firstLevelDivisionDB {

    /** Method executes sql query to gather all pre populated first level division information and save it to the division list.
     * this is all that is needed as the user does not need to add/modify/delete any divisions. */
    public static ObservableList<FirstLevelDivision> getAllDivisions(int countryId) {

        ObservableList<FirstLevelDivision> firstLevelList = FXCollections.observableArrayList();

        try {

            String sqlD = "SELECT Division_ID, Division\n"
                        + "FROM first_level_divisions\n"
                        + "WHERE COUNTRY_ID="
                        + countryId + ";";

            PreparedStatement psDivision = JDBC.getConnection().prepareStatement(sqlD);
            ResultSet resultDivision = psDivision.executeQuery();

            while (resultDivision.next()) {

                int divisionId = resultDivision.getInt("Division_ID");
                String division = resultDivision.getString("Division");

                FirstLevelDivision New = new FirstLevelDivision();
                firstLevelList.add(New);
                New.setDivisionId(divisionId);
                New.setDivision(division);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return firstLevelList;

    }

}
