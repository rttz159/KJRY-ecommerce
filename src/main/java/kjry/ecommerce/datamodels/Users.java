package kjry.ecommerce.datamodels;

import java.util.Date;

public abstract class Users {

    protected String id;
    protected String password;
    protected String name;
    protected String email;
    protected String phoneNo;
    protected char gender;
    protected Date birthDate;
    
    protected Users(String id, String password, String name, String email, String phoneNo, char gender, Date birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public void setPassword(String pw){
        this.password = pw;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

}
