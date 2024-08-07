package kjry.ecommerce.dtos;

import java.util.Date;

public class EmployeesDTO extends UsersDTO{

    public enum JobRoleDTO{
        MANAGER,
        MARKETING,
        STOCK
    }

    private JobRoleDTO jobRole;
    
    public EmployeesDTO(){}
    
    public EmployeesDTO(String id, String password, String name, String email, String phoneNo, char gender, Date birthDate,JobRoleDTO jobrole) {
        super(id, password, name, email, phoneNo, gender, birthDate);
        this.jobRole = jobrole;
    }
    
    public JobRoleDTO getJobRole() {
        return jobRole;
    }

    public void setJobRole(JobRoleDTO jobRole) {
        this.jobRole = jobRole;
    }
    
}
