package kjry.ecommerce.datamodels;

import java.util.Date;

public class Promo implements Comparable{

    private String id;
    private String codeName;
    private Date startingDate;
    private int availableDay;
    private String description;
    private double percentage;
    private boolean isActive = true;

    public Promo() {
    }

    public Promo(String id, String codeName, Date startingDate, int availableDay, String description, double percentage,boolean isActive) {
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

    public void setAvailableDay(int avaiableDay) {
        this.availableDay = avaiableDay;
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

    @Override
    public int compareTo(Object o){
        return this.getId().compareTo(((Promo) o).getId());
    }
}
