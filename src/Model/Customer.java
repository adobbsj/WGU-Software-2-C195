package Model;

/** Model class that defines Customer objects. */
public class Customer {

    int customerId;
    String customerName;
    String customerAddress;
    String postalCode;
    String phoneNumber;
    String Division;
    String Country;

    /** Method that defines customers.
     @param customerId
     @param customerName
     @param customerAddress
     @param postalCode
     @param phoneNumber
     @param Division
     @param Country */
    public Customer(int customerId, String customerName, String customerAddress, String postalCode, String phoneNumber, String Division, String Country) {

        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.Division = Division;
        this.Country = Country;
    }

    /** @return customerId */
    public int getCustomerId() { return customerId; }

    /** @return customerName */
    public String getCustomerName() { return customerName; }

    /** @return customerAddress */
    public String getCustomerAddress() { return customerAddress; }

    /** @return postalCode */
    public String getPostalCode() { return postalCode; }

    /** @return phoneNumber */
    public String getPhoneNumber() { return phoneNumber; }

    /** @return Division */
    public String getDivision() { return Division; }

    /** @return Country */
    public String getCountry() { return Country; }

    @Override
    public String toString() { return (("#" + Integer.toString(customerId) + " " + customerName)); }
}