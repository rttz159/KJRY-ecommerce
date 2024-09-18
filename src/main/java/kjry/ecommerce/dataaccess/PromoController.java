package kjry.ecommerce.dataaccess;

import kjry.ecommerce.datamodels.DynamicList;
import kjry.ecommerce.datamodels.Promo;
import kjry.ecommerce.dtos.PromoDTO;

public class PromoController implements DatabaseController<PromoDTO> {

    @Override
    public PromoDTO findById(String id) {
        PromoDTO temp = null;
        for (Promo promoCode : DatabaseWrapper.getPromoCodeList()) {
            if (promoCode.getId().equals(id)) {
                temp = EntityDTOConverter.convertEntityToDto(promoCode);
                break;
            }
        }
        return temp;
    }

    @Override
    public boolean create(PromoDTO entity) {
        boolean error = true;
        try {
            Promo temp = EntityDTOConverter.convertDtoToEntity(entity);
            DatabaseWrapper.getPromoCodeList().add(temp);
            error = false;
        } catch (Exception ex) {
            System.out.println("Error occurred when creating Promo Code. Promo Code will not be created.");
            ex.printStackTrace();
        }

        return error;
    }

    @Override
    public boolean update(PromoDTO entity) {
        boolean error = true;
        for (int i = 0; i < DatabaseWrapper.getPromoCodeList().size(); i++) {
            if (DatabaseWrapper.getPromoCodeList().get(i).getId().equals(entity.getId())) {
                DatabaseWrapper.getPromoCodeList().set(i, EntityDTOConverter.convertDtoToEntity(entity));
                error = false;
                break;
            }
        }
        return error;
    }

    @Override
    public boolean removeById(String id) {
        boolean error = true;
        for (int i = 0; i < DatabaseWrapper.getPromoCodeList().size(); i++) {
            if (DatabaseWrapper.getPromoCodeList().get(i).getId().equals(id)) {
                DatabaseWrapper.getPromoCodeList().get(i).setIsActive(false);
                error = false;
                break;
            }
        }
        return error;
    }

    @Override
    public DynamicList<PromoDTO> getAll() {
        DynamicList<PromoDTO> temp = new DynamicList<>();
        for (int i = 0; i < DatabaseWrapper.getPromoCodeList().size(); i++) {
            temp.append(EntityDTOConverter.convertEntityToDto(DatabaseWrapper.getPromoCodeList().get(i)));
        }
        return temp;
    }

}
