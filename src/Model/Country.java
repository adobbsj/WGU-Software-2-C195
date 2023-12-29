package Model;

/** Model class that defines Country objects. */
public class Country {
    private int countryId;
    private String country;

    /**Method defines countries.
     @param countryId
     @param country */
    public Country(int countryId, String country) {
        this.countryId = countryId;
        this.country = country;
    }

    /** @param countryId the Id to set */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /** @return countryId */
    public int getCountryId() {
        return countryId;
    }

    /** @param country the Id to set */
    public void setCountry(String country) {
        this.country = country;
    }

    /** @return country */
    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return country;
    }

}