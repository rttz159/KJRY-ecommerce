package kjry.ecommerce.services;

import kjry.ecommerce.dataaccess.DatabaseWrapper;
import kjry.ecommerce.datamodels.Customers;
import kjry.ecommerce.datamodels.Users;

public class NotificationService {
    public static void notify(String message){
        for(int i = 0; i < DatabaseWrapper.getUsersList().size(); i++){
            Users tempUser = DatabaseWrapper.getUsersList().get(i);
            if(tempUser instanceof Customers){
                Customers tempCus = (Customers) tempUser;
                for(Customers.NotificationType x : tempCus.getNotificationTypes()){
                    switch(x){
                        case APP:
                            break;
                        case SMS:
                            System.out.printf("Sending SMS with message <%n%s%n> to %s with phone number of %s.%n%n",message,tempCus.getName(),tempCus.getPhoneNo());
                            break;
                        case EMAIL:
                            System.out.printf("Sending EMAIL with message <%n%s%n> to %s with email of %s.%n%n",message,tempCus.getName(),tempCus.getEmail());
                            break;
                    }
                }
                tempCus.addNotification(message);
            }
        }
    }
}
