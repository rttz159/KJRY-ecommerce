package kjry.ecommerce.dataaccess;

import java.util.ArrayList;
import kjry.ecommerce.datamodels.Pair;
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
import kjry.ecommerce.datamodels.Promo;
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
import kjry.ecommerce.dtos.PromoDTO;
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

            EmployeesDTO tempEmp = new EmployeesDTO(temp.getId(), temp.getPassword(), temp.getName(), temp.getEmail(), temp.getPhoneNo(), temp.getGender(), temp.getBirthDate(), jobroleDTO);
            tempEmp.setIsActive(temp.isIsActive());
            dto = tempEmp;
        } else {
            Customers temp = (Customers) user;
            ArrayList<Pair<ProductsDTO, Integer>> productArr = new ArrayList<>();
            for (Pair<Products, Integer> x : temp.getShoppingCart()) {
                productArr.add(new Pair(convertEntityToDto(x.getKey()), x.getValue()));
            }
            ArrayList<NotificationTypeDTO> notificationArr = new ArrayList<>();
            for (NotificationType x : temp.getNotificationTypes()) {
                if (x == NotificationType.SMS) {
                    notificationArr.add(NotificationTypeDTO.SMS);
                } else if (x == NotificationType.EMAIL) {
                    notificationArr.add(NotificationTypeDTO.EMAIL);
                } else if (x == NotificationType.APP) {
                    notificationArr.add(NotificationTypeDTO.APP);
                }
            }

            CustomersDTO tempCusDTO = new CustomersDTO(temp.getId(), temp.getPassword(), temp.getName(), temp.getEmail(), temp.getPhoneNo(), temp.getGender(), temp.getBirthDate(), notificationArr, productArr);
            for (String message : temp.getNotification()) {
                tempCusDTO.addNotification(message);
            }
            tempCusDTO.setIsActive(temp.isIsActive());
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

            productDto = new ClothingDTO(tempCloth.getId(), tempCloth.getName(), tempCloth.getCostPrice(), tempCloth.getSellingPrice(), DatabaseWrapper.getProductStock().getOrDefault(product, -1), sizeDto, typeDto);
            if (tempCloth.getImagePath() != null) {
                productDto.setImagePath(tempCloth.getImagePath());
            }
            productDto.setIsActive(tempCloth.isIsActive());
        } else if (product instanceof Accessories) {
            Accessories tempAcc = (Accessories) product;
            productDto = new AccessoriesDTO(tempAcc.getId(), tempAcc.getName(), tempAcc.getCostPrice(), tempAcc.getSellingPrice(), DatabaseWrapper.getProductStock().getOrDefault(product, -1), tempAcc.isWashable());
            if (tempAcc.getImagePath() != null) {
                productDto.setImagePath(tempAcc.getImagePath());
            }
            productDto.setIsActive(tempAcc.isIsActive());
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

        PromoDTO tempPromo = null;
        if (order.getPromoUsed() != null) {
            tempPromo = convertEntityToDto(order.getPromoUsed());
        }

        return new OrdersDTO(order.getId(), order.getAddress(), userDto, statusDto, productListsDto, order.getOrderingDate(), tempPromo);
    }

    public static PromoDTO convertEntityToDto(Promo promo) {
        return new PromoDTO(promo.getId(), promo.getCodeName(), promo.getStartingDate(), promo.getAvailableDay(), promo.getDescription(), promo.getPercentage(), promo.isIsActive());
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
            user.setIsActive(tempDto.getIsActive());
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
                if (x == NotificationTypeDTO.SMS) {
                    notificationArr.add(NotificationType.SMS);
                } else if (x == NotificationTypeDTO.EMAIL) {
                    notificationArr.add(NotificationType.EMAIL);
                } else if (x == NotificationTypeDTO.APP) {
                    notificationArr.add(NotificationType.APP);
                }
            }
            Customers tempCust = new Customers(tempDto.getId(), tempDto.getPassword(), tempDto.getName(), tempDto.getEmail(), tempDto.getPhoneNo(), tempDto.getGender(), tempDto.getBirthDate(), notificationArr, productArr);
            for (String message : tempDto.getNotification()) {
                tempCust.addNotification(message);
            }
            user = tempCust;
            user.setIsActive(tempDto.getIsActive());
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
            if (tempClothDto.getImagePath() != null) {
                product.setImagePath(tempClothDto.getImagePath());
            }
            product.setIsActive(tempClothDto.getIsActive());
        } else if (productDto instanceof AccessoriesDTO) {
            AccessoriesDTO tempAccDto = (AccessoriesDTO) productDto;
            product = new Accessories(tempAccDto.getId(), tempAccDto.getName(), tempAccDto.getCostPrice(), tempAccDto.getSellingPrice(), tempAccDto.isWashable());
            if (tempAccDto.getImagePath() != null) {
                product.setImagePath(tempAccDto.getImagePath());
            }
            product.setIsActive(tempAccDto.getIsActive());
        }

        if (productDto.getStockQty() != -1) {
            DatabaseWrapper.getProductStock().replace(product, productDto.getStockQty());
        }

        return product;
    }

    public static Orders convertDtoToEntity(OrdersDTO orderDto) {
        Users user = null;
        for (int i = 0; i < DatabaseWrapper.getUsersList().size(); i++) {
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
        
        Promo tempPromo = null;
        if (orderDto.getPromo() != null) {
            tempPromo = convertDtoToEntity(orderDto.getPromo());
        }
        if (orderDto.getId() == null) {
            return new Orders(orderDto.getAddress(), user, status, productLists, tempPromo);
        } else {
            return new Orders(orderDto.getId(), orderDto.getAddress(), user, status, productLists, orderDto.getOrderingDate(), tempPromo);
        }
    }

    public static Promo convertDtoToEntity(PromoDTO dto) {
        return new Promo(dto.getId(), dto.getCodeName(), dto.getStartingDate(), dto.getAvailableDay(), dto.getDescription(), dto.getPercentage(), dto.isIsActive());
    }
}
