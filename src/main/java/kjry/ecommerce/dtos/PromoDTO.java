package kjry.ecommerce.dtos;

import java.util.Date;

public class PromoDTO {

    private String id;
    private String codeName;
    private Date startingDate;
    private int availableDay;
    private String description;
    private double percentage;
    private boolean isActive;

    public PromoDTO() {
    }

    public PromoDTO(String id, String codeName, Date startingDate, int availableDay, String description, double percentage,boolean isActive) {
        this.id = id;
        this.codeName = codeName;
        this.startingDate = startingDate;
        this.availableDay = availableDay;
        this.percentage = percentage;
        this.description = description;
        this.isActive = isActive;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodeName() {
        return this.codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public int getAvailableDay() {
        return availableDay;
    }

    public void setAvailableDay(int availableDay) {
        this.availableDay = availableDay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

}
