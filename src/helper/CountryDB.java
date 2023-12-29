package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Helper class for Querying all countries. */
public class CountryDB {

    /** Method executes sql query to gather all pre populated country information and save it to the country list.
     * this is all that is needed as the user does not need to add/modify/delete countries. */
    public static ObservableList<String> getAllCountries(){

        ObservableList<String> countryList = FXCollections.observableArrayList();

        try {
            String sqlCountry = """
                    SELECT Country
                    FROM countries
                    WHERE Country_ID <= 3;""";

            PreparedStatement psCountry = JDBC.getConnection().prepareStatement(sqlCountry);
            ResultSet resultCountry = psCountry.executeQuery();

            while(resultCountry.next()){
                String countryName = resultCountry.getString("Country");
                countryList.add(countryName);
            }
        }

        catch(SQLException e){
            e.printStackTrace();
        }

        return countryList;
    }
}
