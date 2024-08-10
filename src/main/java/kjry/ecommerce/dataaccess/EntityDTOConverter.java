package kjry.ecommerce.dataaccess;

import java.util.ArrayList;
import javafx.util.Pair;
import kjry.ecommerce.datamodels.Accessories;
import kjry.ecommerce.datamodels.Clothing;
import kjry.ecommerce.datamodels.Clothing.Size;
import static kjry.ecommerce.datamodels.Clothing.Size.*;
import kjry.ecommerce.datamodels.Clothing.Type;
import static kjry.ecommerce.datamodels.Clothing.Type.*;
import kjry.ecommerce.datamodels.Employees;
import kjry.ecommerce.datamodels.Customers;
import kjry.ecommerce.datamodels.Customers.NotificationType;
import kjry.ecommerce.datamodels.Employees.JobRole;
import kjry.ecommerce.datamodels.Orders;
import static kjry.ecommerce.datamodels.Orders.Status.*;
import kjry.ecommerce.datamodels.Products;
import kjry.ecommerce.datamodels.Users;
import kjry.ecommerce.dtos.AccessoriesDTO;
import kjry.ecommerce.dtos.ClothingDTO;
import kjry.ecommerce.dtos.ClothingDTO.SizeDTO;
import kjry.ecommerce.dtos.ClothingDTO.TypeDTO;
import kjry.ecommerce.dtos.EmployeesDTO;
import kjry.ecommerce.dtos.CustomersDTO;
import kjry.ecommerce.dtos.CustomersDTO.NotificationTypeDTO;
import kjry.ecommerce.dtos.EmployeesDTO.JobRoleDTO;
import kjry.ecommerce.dtos.OrdersDTO;
import kjry.ecommerce.dtos.ProductsDTO;
import kjry.ecommerce.dtos.UsersDTO;

public class EntityDTOConverter {

    public static UsersDTO convertEntityToDto(Users user) {
        UsersDTO dto = null;
        if (user instanceof Employees) {
            Employees temp = (Employees) user;
            JobRole jobrole = temp.getJobRole();
            JobRoleDTO jobroleDTO;
            switch (jobrole) {
                case MANAGER:
                    jobroleDTO = JobRoleDTO.MANAGER;
                    break;
                case MARKETING:
                    jobroleDTO = JobRoleDTO.MARKETING;
                    break;
                default:
                    jobroleDTO = JobRoleDTO.STOCK;
                    break;
            }

            dto = new EmployeesDTO(temp.getId(), temp.getPassword(), temp.getName(), temp.getEmail(), temp.getPhoneNo(), temp.getGender(), temp.getBirthDate(), jobroleDTO);
        } else {
            Customers temp = (Customers) user;
            ArrayList<Pair<ProductsDTO, Integer>> productArr = new ArrayList<>();
            for (Pair<Products, Integer> x : temp.getShoppingCart()) {
                productArr.add(new Pair(convertEntityToDto(x.getKey()), x.getValue()));
            }
            ArrayList<NotificationTypeDTO> notificationArr = new ArrayList<>();
            for (NotificationType x : temp.getNotificationTypes()) {
                switch (x) {
                    case SMS:
                        notificationArr.add(NotificationTypeDTO.SMS);
                        break;
                    case EMAIL:
                        notificationArr.add(NotificationTypeDTO.EMAIL);
                        break;
                    case APP:
                        notificationArr.add(NotificationTypeDTO.EMAIL);
                        break;
                }
            }

            CustomersDTO tempCusDTO = new CustomersDTO(temp.getId(), temp.getPassword(), temp.getName(), temp.getEmail(), temp.getPhoneNo(), temp.getGender(), temp.getBirthDate(), notificationArr, productArr);
            for (String message : temp.getNotification()) {
                tempCusDTO.addNotification(message);
            }
            dto = tempCusDTO;
        }

        return dto;
    }

    public static ProductsDTO convertEntityToDto(Products product) {
        ProductsDTO productDto = null;
        if (product instanceof Clothing) {
            Clothing tempCloth = (Clothing) product;

            TypeDTO typeDto = null;
            switch (tempCloth.getType()) {
                case SHIRT:
                    typeDto = TypeDTO.SHIRT;
                    break;
                case PANTS:
                    typeDto = TypeDTO.PANTS;
                    break;
                case SKIRT:
                    typeDto = TypeDTO.SKIRT;
                    break;
            }

            SizeDTO sizeDto = null;
            switch (tempCloth.getSize()) {
                case S:
                    sizeDto = SizeDTO.S;
                    break;
                case M:
                    sizeDto = SizeDTO.M;
                    break;
                case L:
                    sizeDto = SizeDTO.L;
                    break;
            }

            productDto = new ClothingDTO(tempCloth.getId(), tempCloth.getName(), tempCloth.getCostPrice(), tempCloth.getSellingPrice(), tempCloth.getImagePath(), DatabaseWrapper.getProductStock().get(product), sizeDto, typeDto);
        } else if (product instanceof Accessories) {
            Accessories tempAcc = (Accessories) product;
            productDto = new AccessoriesDTO(tempAcc.getId(), tempAcc.getName(), tempAcc.getCostPrice(), tempAcc.getSellingPrice(), tempAcc.getImagePath(), DatabaseWrapper.getProductStock().get(product), tempAcc.isWashable());
        }

        return productDto;
    }

    public static OrdersDTO convertEntityToDto(Orders order) {
        UsersDTO userDto = convertEntityToDto(order.getUser());
        OrdersDTO.StatusDTO statusDto = null;
        switch (order.getStatus()) {
            case DONE:
                statusDto = OrdersDTO.StatusDTO.DONE;
                break;
            case PROCESSING:
                statusDto = OrdersDTO.StatusDTO.PROCESSING;
                break;
            case PENDING:
                statusDto = OrdersDTO.StatusDTO.PENDING;
                break;
            case CANCELLED:
                statusDto = OrdersDTO.StatusDTO.CANCELLED;
                break;
        }

        ArrayList<Pair<ProductsDTO, Integer>> productListsDto = new ArrayList<>();
        for (Pair<Products, Integer> pair : order.getProductLists()) {
            ProductsDTO productDto = convertEntityToDto(pair.getKey());
            productListsDto.add(new Pair<>(productDto, pair.getValue()));
        }

        return new OrdersDTO(order.getId(), order.getAddress(), userDto, statusDto, productListsDto, order.getOrderingDate());
    }

    public static Users convertDtoToEntity(UsersDTO dto) {
        Users user = null;
        if (dto instanceof EmployeesDTO) {
            EmployeesDTO tempDto = (EmployeesDTO) dto;
            JobRole jobRole = null;
            switch (tempDto.getJobRole()) {
                case MANAGER:
                    jobRole = JobRole.MANAGER;
                    break;
                case MARKETING:
                    jobRole = JobRole.MARKETING;
                    break;
                case STOCK:
                    jobRole = JobRole.STOCK;
                    break;
            }
            user = new Employees(tempDto.getId(), tempDto.getPassword(), tempDto.getName(), tempDto.getEmail(), tempDto.getPhoneNo(), tempDto.getGender(), tempDto.getBirthDate(), jobRole);
        } else if (dto instanceof CustomersDTO) {
            CustomersDTO tempDto = (CustomersDTO) dto;
            ArrayList<Pair<Products, Integer>> productArr = new ArrayList<>();
            for (Pair<ProductsDTO, Integer> x : tempDto.getShoppingCart()) {
                Products prod = null;
                for (int i = 0; i < DatabaseWrapper.getProductsList().size(); i++) {
                    if (DatabaseWrapper.getProductsList().get(i).getId().equals(convertDtoToEntity(x.getKey()).getId())) {
                        prod = DatabaseWrapper.getProductsList().get(i);
                    }
                }
                productArr.add(new Pair(prod, x.getValue()));
            }
            ArrayList<NotificationType> notificationArr = new ArrayList<>();
            for (NotificationTypeDTO x : tempDto.getNotificationTypes()) {
                switch (x) {
                    case SMS:
                        notificationArr.add(NotificationType.SMS);
                        break;
                    case EMAIL:
                        notificationArr.add(NotificationType.EMAIL);
                        break;
                    case APP:
                        notificationArr.add(NotificationType.APP);
                        break;
                }
            }
            Customers tempCust = new Customers(tempDto.getId(), tempDto.getPassword(), tempDto.getName(), tempDto.getEmail(), tempDto.getPhoneNo(), tempDto.getGender(), tempDto.getBirthDate(), notificationArr, productArr);
            for (String message : tempDto.getNotification()) {
                tempCust.addNotification(message);
            }
            user = tempCust;
        }

        return user;
    }

    public static Products convertDtoToEntity(ProductsDTO productDto) {
        Products product = null;
        if (productDto instanceof ClothingDTO) {
            ClothingDTO tempClothDto = (ClothingDTO) productDto;

            Type type = null;
            switch (tempClothDto.getClothingType()) {
                case SHIRT:
                    type = Type.SHIRT;
                    break;
                case PANTS:
                    type = Type.PANTS;
                    break;
                case SKIRT:
                    type = Type.SKIRT;
                    break;
            }

            Size size = null;
            switch (tempClothDto.getSize()) {
                case S:
                    size = Size.S;
                    break;
                case M:
                    size = Size.M;
                    break;
                case L:
                    size = Size.L;
                    break;
            }

            product = new Clothing(tempClothDto.getId(), tempClothDto.getName(), tempClothDto.getCostPrice(), tempClothDto.getSellingPrice(), size, type);
            product.setImagePath(tempClothDto.getImagePath());
        } else if (productDto instanceof AccessoriesDTO) {
            AccessoriesDTO tempAccDto = (AccessoriesDTO) productDto;
            product = new Accessories(tempAccDto.getId(), tempAccDto.getName(), tempAccDto.getCostPrice(), tempAccDto.getSellingPrice(), tempAccDto.isWashable());
            product.setImagePath(tempAccDto.getImagePath());
        }
        
        if (productDto.getStockQty() != -1) {
            DatabaseWrapper.getProductStock().replace(product, productDto.getStockQty());
        }

        return product;
    }

    public static Orders convertDtoToEntity(OrdersDTO orderDto) {
        Users user = null;
        for (int i = 0; i < DatabaseWrapper.getProductsList().size(); i++) {
            if (DatabaseWrapper.getUsersList().get(i).getId().equals(convertDtoToEntity(orderDto.getUser()).getId())) {
                user = DatabaseWrapper.getUsersList().get(i);
            }
        }
        Orders.Status status = null;
        switch (orderDto.getStatus()) {
            case DONE:
                status = Orders.Status.DONE;
                break;
            case PROCESSING:
                status = Orders.Status.PROCESSING;
                break;
            case PENDING:
                status = Orders.Status.PENDING;
                break;
            case CANCELLED:
                status = Orders.Status.CANCELLED;
                break;
        }

        ArrayList<Pair<Products, Integer>> productLists = new ArrayList<>();
        for (Pair<ProductsDTO, Integer> pair : orderDto.getProductLists()) {
            Products product = null;
            for (int i = 0; i < DatabaseWrapper.getProductsList().size(); i++) {
                if (DatabaseWrapper.getProductsList().get(i).getId().equals(convertDtoToEntity(pair.getKey()).getId())) {
                    product = DatabaseWrapper.getProductsList().get(i);
                }
            }
            productLists.add(new Pair<>(product, pair.getValue()));
        }

        if (orderDto.getId() == null) {
            return new Orders(orderDto.getAddress(), user, status, productLists);
        } else {
            return new Orders(orderDto.getId(), orderDto.getAddress(), user, status, productLists, orderDto.getOrderingDate());
        }
    }

}
