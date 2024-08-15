package kjry.ecommerce.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import kjry.ecommerce.datamodels.Pair;
import kjry.ecommerce.dataaccess.DatabaseController;
import kjry.ecommerce.dataaccess.UserController;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.dtos.ProductsDTO;
import kjry.ecommerce.dtos.UsersDTO;

public class UserService {

    private UsersDTO entityToProcess;
    private DatabaseController dbController;

    public UserService(UsersDTO entity) {
        this.entityToProcess = entity;
        this.dbController = new UserController();
    }

    public static UsersDTO getUser(String id) {
        DatabaseController dbController2 = new UserController();
        return (UsersDTO) dbController2.findById(id);
    }

    public static boolean createUser(UsersDTO dto) {
        if (dto != null) {
            DatabaseController dbController2 = new UserController();
            return dbController2.create(dto);
        } else {
            return false;
        }
    }

    public boolean updateUser() {
        if (entityToProcess != null) {
            return dbController.update(entityToProcess);
        } else {
            return false;
        }
    }

    public boolean deleteUser(String id) {
        return dbController.removeById(id);
    }

    public boolean deleteUser() {
        if (entityToProcess != null) {
            return dbController.removeById(entityToProcess.getId());
        } else {
            return false;
        }
    }

    public static boolean validateLogin(String id, String password) {
        boolean existed = false;

        DatabaseController dbController2 = new UserController();
        UsersDTO tempUserDTO = (UsersDTO) dbController2.findById(id);
        if (tempUserDTO != null) {
            if (tempUserDTO.getPassword().equals(password)) {
                existed = true;
            }
        }

        return existed;
    }

    public boolean appendShoppingCart(ProductsDTO dto, int num) {
        if (entityToProcess != null && entityToProcess instanceof CustomersDTO) {
            CustomersDTO temp = (CustomersDTO) entityToProcess;
            temp.getShoppingCart().add(new Pair(dto, num));
            return dbController.update(entityToProcess);
        } else {
            return false;
        }
    }

    public boolean removeProductInShoppingCart(ProductsDTO dto) {
        if (entityToProcess != null && entityToProcess instanceof CustomersDTO) {
            CustomersDTO temp = (CustomersDTO) entityToProcess;
            for (Pair<ProductsDTO, Integer> x : temp.getShoppingCart()) {
                if (x.getKey().getId().equals(dto.getId())) {
                    temp.getShoppingCart().remove(x);
                }
            }

            return dbController.update(entityToProcess);
        } else {
            return false;
        }
    }

    public Pair<ProductsDTO, Integer>[] getShoppingCartList() {
        if (entityToProcess != null && entityToProcess instanceof CustomersDTO) {
            CustomersDTO temp = (CustomersDTO) entityToProcess;
            ArrayList<Pair<ProductsDTO, Integer>> tempProdList = temp.getShoppingCart();
            Pair<ProductsDTO, Integer>[] prodArray = new Pair[tempProdList.size()];
            for (int i = 0; i < prodArray.length; i++) {
                prodArray[i] = tempProdList.get(i);
            }
            return prodArray;
        } else {
            return null;
        }
    }

    public static UsersDTO[] getAllUsers(boolean withInactive) {
        DatabaseController dbController2 = new UserController();
        ArrayList<UsersDTO> temp = new ArrayList<>();
        Collections.addAll(temp, (UsersDTO[]) dbController2.getAll());
        if (!withInactive) {
            Iterator<UsersDTO> iterator = temp.iterator();
            while (iterator.hasNext()) {
                UsersDTO user = iterator.next();
                if (!user.getIsActive()) {
                    iterator.remove();
                }
            }
        }

        UsersDTO[] tempArr = new UsersDTO[temp.size()];
        int i = 0;
        for (UsersDTO user : temp) {
            tempArr[i] = user;
            i++;
        }
        return tempArr;
    }

    public String[] getAllNotification() {
        if (entityToProcess != null && entityToProcess instanceof CustomersDTO) {
            CustomersDTO temp = (CustomersDTO) entityToProcess;
            String[] tempArr = new String[temp.getNotification().size()];
            for (int i = 0; i < tempArr.length; i++) {
                tempArr[i] = temp.getNotification().get(i);
            }
            return tempArr;
        } else {
            return null;
        }
    }

}
