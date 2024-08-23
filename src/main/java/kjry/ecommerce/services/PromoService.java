package kjry.ecommerce.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import kjry.ecommerce.dataaccess.DatabaseController;
import kjry.ecommerce.dataaccess.PromoController;
import kjry.ecommerce.dtos.PromoDTO;

public class PromoService {

    private static DatabaseController dbController = new PromoController();

    public static PromoDTO[] getAllPromo(boolean withInactive) {
        ArrayList<PromoDTO> temp = new ArrayList<>();
        Collections.addAll(temp, (PromoDTO[]) dbController.getAll());
        if (!withInactive) {
            Iterator<PromoDTO> iterator = temp.iterator();
            while (iterator.hasNext()) {
                PromoDTO promo = iterator.next();
                if (!promo.isIsActive()) {
                    iterator.remove();
                }
            }
        }
        PromoDTO[] tempArr = new PromoDTO[temp.size()];
        int i = 0;
        for (PromoDTO promo : temp) {
            tempArr[i] = promo;
            i++;
        }
        return tempArr;
    }

    public static boolean createPromo(PromoDTO dto) {
        if (dto != null) {
            return dbController.create(dto);
        } else {
            return false;
        }
    }

    public static boolean updatePromo(PromoDTO dto) {
        if (dto != null) {
            return dbController.update(dto);
        } else {
            return false;
        }
    }

    public static boolean removePromo(String id) {
        if (id != null) {
            return dbController.removeById(id);
        } else {
            return false;
        }
    }
    
    public static boolean removePromoByCodeName(String codeName) {
        if (codeName != null) {
            PromoDTO[] temp = getAllPromo(false);
            String id = "";
            for(PromoDTO x : temp){
                if(x.getCodeName().equalsIgnoreCase(codeName)){
                    id = x.getId();
                    break;
                }
            }
            if(!id.isEmpty()){
                return dbController.removeById(id);
            }else{
                return false;
            }
        } else {
            return false;
        }
    }

}
