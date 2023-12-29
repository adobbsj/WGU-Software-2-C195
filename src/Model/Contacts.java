package Model;

/** Model class that defines Contact objects. */
public class Contacts {
    int contactId;
    String contactName;
    String contactEmail;


    @Override
    public String toString() {
        return ((/*"#" + Integer.toString(contactId) + " " + */contactName));
    }

    /**Method that defines contacts.
     @param contactId
     @param contactName */
    public Contacts(int contactId, String contactName) {
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /** @return contactId */
    public int getContactId() {
        return contactId;
    }

    /** @return contactName */
    public String getContactName() {
        return contactName;
    }
}
