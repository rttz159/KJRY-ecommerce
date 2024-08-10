package kjry.ecommerce.datamodels;

import java.util.Date;

public class Employees extends Users {

    private JobRole jobRole;

    public enum JobRole {
        MANAGER,
        MARKETING,
        STOCK
    }

    public Employees(String id, String password, String name, String email, String phoneNo, char gender, Date birthDate, JobRole jobrole) {
        super(id, password, name, email, phoneNo, gender, birthDate);
        this.jobRole = jobrole;
    }
    
    public Employees(){}

    public JobRole getJobRole() {
        return jobRole;
    }

    public void setJobRole(JobRole jobRole) {
        this.jobRole = jobRole;
    }

}
