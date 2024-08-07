package kjry.ecommerce.datamodels;

import com.google.gson.annotations.Expose;
import java.util.Date;

public class Employees extends Users{

    public enum JobRole{
        MANAGER,
        MARKETING,
        STOCK
    }
    
    @Expose
    private JobRole jobRole;
    
    public Employees(String id, String password, String name, String email, String phoneNo, char gender, Date birthDate,JobRole jobrole) {
        super(id, password, name, email, phoneNo, gender, birthDate);
        this.jobRole = jobrole;
    }
    
    public JobRole getJobRole() {
        return jobRole;
    }

    public void setJobRole(JobRole jobRole) {
        this.jobRole = jobRole;
    }
    
}
