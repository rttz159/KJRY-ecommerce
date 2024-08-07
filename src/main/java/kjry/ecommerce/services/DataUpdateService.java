package kjry.ecommerce.services;

import kjry.ecommerce.dataaccess.DatabaseWrapper;

public class DataUpdateService {
    public static void save(){
        DatabaseWrapper.updateFile();;
    }
}
