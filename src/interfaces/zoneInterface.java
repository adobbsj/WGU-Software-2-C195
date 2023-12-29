package interfaces;

import java.time.ZoneId;

/** Interface for use with lambda expression. displays zone Id. */
public interface zoneInterface {

    /** Method which is used on the login page for use with lambda expression. Displays Users current zone. */
    //lambda expression used with this interface to display the users location on the screen.
    String getZone(ZoneId thisZone);
}
