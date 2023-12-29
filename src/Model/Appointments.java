package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.time.LocalTime;

/** Model class that defines appointments. */
public class Appointments {
    int Id;
    String title;
    String description;
    String location;
    String type;
    LocalDateTime startTime;
    LocalDateTime endTime;
    int CustomerId;
    int UserId;
    int ContactId;

    /**Method that defines the appointments.
     @param Id
     @param title
     @param description
     @param location
     @param type
     @param startTime
     @param endTime
     @param CustomerId
     @param UserId
     @param ContactId */
    public Appointments( int Id, String title, String description, String location, String type, LocalDateTime startTime, LocalDateTime endTime, int CustomerId, int UserId, int ContactId){

        this.Id = Id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.CustomerId = CustomerId;
        this.UserId = UserId;
        this.ContactId = ContactId;
    }


    public Appointments() {
    }


    /** @return Id */
    public int getId() {
        return Id;
    }

    /** @param Id the Id to set */
    public void setId(int Id) { this.Id = Id; }

    /** @return title */
    public String getTitle() {
        return title;
    }

    /** @param title the title to set */
    public void setTitle(String title) { this.title = title; }

    /** @return description */
    public String getDescription() {
        return description;
    }

    /** @param description the description to set */
    public void setDescription(String description) { this.description = description; }

    /** @return location */
    public String getLocation() {
        return location;
    }

    /** @param location the description to set */
    public void setLocation(String location) { this.location = location; }

    /** @return type */
    public String getType() {
        return type;
    }

    /** @param type the type to set */
    public void setType(String type) { this.type = type; }

    /** @return startTime */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /** @return endTime */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /** @return CustomerId */
    public int getCustomerId() { return CustomerId; }

    /** @param customerId the customerId to set */
    public void setCustomerId(int customerId) { CustomerId = customerId; }

    /** @return ContactId */
    public int getContactId() { return ContactId; }

    /** @return UserId */
    public int getUserId() { return UserId; }

    /** Method sets a list of times by 30 minute increments for scheduling. */
    public static ObservableList<LocalTime> getTimes(){
        ObservableList<LocalTime> timesList = FXCollections.observableArrayList();

        LocalTime start = LocalTime.of(1,00);
        LocalTime end = LocalTime.MIDNIGHT.minusHours(1);

        while(start.isBefore(end.plusSeconds(1))){
            timesList.add(start);
            start = start.plusMinutes(30);

        }
        return timesList;
    }
}
