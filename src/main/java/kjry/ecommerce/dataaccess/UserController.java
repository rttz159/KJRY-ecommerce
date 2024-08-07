package kjry.ecommerce.dataaccess;

import kjry.ecommerce.datamodels.Users;
import kjry.ecommerce.dtos.UsersDTO;

public class UserController implements DatabaseController<UsersDTO> {
    
    @Override
    public UsersDTO findById(String id) {
        UsersDTO temp = null;
        for(Users user: DatabaseWrapper.getUsersList()){
            if(user.getId().equals(id)){
                temp = EntityDTOConverter.convertEntityToDto(user);
                break;
            }
        }
        return temp;
    }

    @Override
    public boolean create(UsersDTO entity) {
        boolean error = true;
        try {
            Users temp = EntityDTOConverter.convertDtoToEntity(entity);
            DatabaseWrapper.getUsersList().add(temp);
            error = false;
        } catch (Exception ex) {
            System.out.println("Error occured when creating User. User will not be created");
        }
        return error;
    }

    @Override
    public boolean update(UsersDTO entity) {
        boolean error = true;
        for(int i = 0; i < DatabaseWrapper.getUsersList().size(); i++){
            if(DatabaseWrapper.getUsersList().get(i).getId().equals(entity.getId())){
                DatabaseWrapper.getUsersList().set(i, EntityDTOConverter.convertDtoToEntity(entity));
                error = false;
                break;
            }
        }
        return error;
    }

    @Override
    public boolean removeById(String id) {
        boolean error = true;
        for(int i = 0; i < DatabaseWrapper.getUsersList().size(); i++){
            if(DatabaseWrapper.getUsersList().get(i).getId().equals(id)){
                DatabaseWrapper.getUsersList().remove(i);
                error = false;
                break;
            }
        }
        return error;
    }
    
    @Override
    public UsersDTO[] getAll() {
        UsersDTO[] temp = new UsersDTO[DatabaseWrapper.getUsersList().size()];
        for(int i = 0; i < DatabaseWrapper.getUsersList().size(); i++){
            temp[i] = EntityDTOConverter.convertEntityToDto(DatabaseWrapper.getUsersList().get(i));
        }
        return temp;
    }
    
}
