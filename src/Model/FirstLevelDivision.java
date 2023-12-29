package Model;

/** Model class that defines First Level Division objects. */
public class FirstLevelDivision {
    private int divisionId;
    private String division;



    /**Method defines first level divisions.
     @param divisionId
     @param division */
    public FirstLevelDivision(int divisionId, String division) {
        this.divisionId = divisionId;
        this.division = division;

    }

    public FirstLevelDivision(){
    }

    /** @param divisionId the division Id to set */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /** @return divisionId */
    public int getDivisionId() {
        return divisionId;
    }

    /** @param division the division to set */
    public void setDivision(String division) {
        this.division = division;
    }

    /** @return division */
    public  String getDivision() {
        return division;
    }

    @Override
    public String toString() {
        return division;
    }
}
