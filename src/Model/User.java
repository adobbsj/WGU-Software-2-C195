package Model;

/** Model class that defines User objects. */
public class User {
    private final int id;
    private final String userName;
    private String password;

    /**Method defines users.
     @param id
     @param userName */
    public User(int id, String userName){
        this.id = id;
        this.userName = userName;
    }

    /**Method defines users with password protection.
     @param id
     @param userName
     @param password */
    public User(int id, String userName, String password){

        this.id = id;
        this.userName = userName;
        this.password = password;
    }
    /** @return id */
    public int getId() { return id; }

    /** @return userName */
    public String getUserName(){ return userName; }

    /** @return password */
    public String getPassword(){ return password; }

    @Override
    public String toString(){
        return(("#" + Integer.toString(id) + " " + userName));
    }
}
