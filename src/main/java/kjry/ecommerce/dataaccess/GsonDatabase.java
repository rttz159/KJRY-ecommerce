package kjry.ecommerce.dataaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.nio.file.Files;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import kjry.ecommerce.datamodels.Pair;
import kjry.ecommerce.datamodels.Accessories;
import kjry.ecommerce.datamodels.Clothing;
import kjry.ecommerce.datamodels.Customers;
import kjry.ecommerce.datamodels.Employees;
import kjry.ecommerce.datamodels.Orders;
import kjry.ecommerce.datamodels.Products;
import kjry.ecommerce.datamodels.Promo;
import kjry.ecommerce.datamodels.Users;

public class GsonDatabase extends Database {

    private GsonBuilder gsonbuilder;
    private Gson gson;

    public GsonDatabase(String filepath) {
        this.gsonbuilder = new GsonBuilder()
                .registerTypeAdapter(Users.class, new UsersAdapter())
                .registerTypeAdapter(new TypeToken<ArrayList<Users>>() {
                }.getType(), new UsersListAdapter())
                .registerTypeAdapter(Products.class, new ProductsAdapter())
                .registerTypeAdapter(new TypeToken<ArrayList<Products>>() {
                }.getType(), new ProductsListAdapter())
                .registerTypeAdapter(Orders.class, new OrdersAdapter())
                .registerTypeAdapter(new TypeToken<ArrayList<Orders>>() {
                }.getType(), new OrdersListAdapter())
                .registerTypeAdapter(new TypeToken<HashMap<Products, Integer>>() {
                }.getType(), new ProductStockAdapter());
        this.gson = gsonbuilder.create();
        this.orderList = new ArrayList<>();
        this.productList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.promoList = new ArrayList<>();
        this.productStock = new HashMap<>();
        readFile(filepath);
    }

    @Override
    public boolean readFile(String filepath) {
        boolean error = true;
        try {
            String jsonStr = Files.readString(Paths.get(filepath));
            JsonObject jsonObject = gson.fromJson(jsonStr, JsonObject.class);
            JsonElement usersElement = jsonObject.get("users");
            JsonElement productsElement = jsonObject.get("products");
            JsonElement ordersElement = jsonObject.get("orders");
            JsonElement promoCodeElement = jsonObject.get("promoCode");
            JsonElement productStockElement = jsonObject.get("productStock");
            this.userList = gson.fromJson(usersElement, new TypeToken<ArrayList<Users>>() {
            }.getType());
            this.productList = gson.fromJson(productsElement, new TypeToken<ArrayList<Products>>() {
            }.getType());
            this.promoList = gson.fromJson(promoCodeElement, new TypeToken<ArrayList<Promo>>() {
            }.getType());
            this.orderList = gson.fromJson(ordersElement, new TypeToken<ArrayList<Orders>>() {
            }.getType());
            this.productStock = gson.fromJson(productStockElement, new TypeToken<HashMap<Products, Integer>>() {
            }.getType());
            error = false;
        } catch (IOException e) {
            createFile(filepath);
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            System.out.println("Error occured when parsing JSON String.");
        }
        return error;
    }

    @Override
    public boolean createFile(String filepath) {
        boolean error = true;
        try {
            FileWriter fw = new FileWriter(filepath);
            fw.write("");
            fw.close();
            error = false;
        } catch (IOException e) {
            System.out.println("Error occured when creating file.");
        }
        return error;
    }

    @Override
    public boolean updateFile(String filepath) {
        boolean error = true;
        try (FileWriter fw = new FileWriter(filepath)) {
            JsonObject jsonObject = new JsonObject();
            JsonArray usersJsonArray = gson.toJsonTree(userList, new TypeToken<ArrayList<Users>>() {
            }.getType()).getAsJsonArray();
            JsonArray productsJsonArray = gson.toJsonTree(productList, new TypeToken<ArrayList<Products>>() {
            }.getType()).getAsJsonArray();
            JsonArray promoCodeJsonArray = gson.toJsonTree(promoList, new TypeToken<ArrayList<Promo>>() {
            }.getType()).getAsJsonArray();
            JsonArray ordersJsonArray = gson.toJsonTree(orderList, new TypeToken<ArrayList<Orders>>() {
            }.getType()).getAsJsonArray();
            JsonElement productStockJson = gson.toJsonTree(productStock, new TypeToken<HashMap<Products, Integer>>() {
            }.getType());
            jsonObject.add("users", usersJsonArray);
            jsonObject.add("products", productsJsonArray);
            jsonObject.add("orders", ordersJsonArray);
            jsonObject.add("promoCode", promoCodeJsonArray);
            jsonObject.add("productStock", productStockJson);
            fw.write(gson.toJson(jsonObject));
            error = false;
        } catch (IOException e) {
            System.out.println("Error occured when writing to file.");
        }
        return error;
    }

    private class UsersListAdapter implements JsonSerializer<ArrayList<Users>>, JsonDeserializer<ArrayList<Users>> {

        private final UsersAdapter usersAdapter = new UsersAdapter();

        @Override
        public JsonElement serialize(ArrayList<Users> userList, Type type, JsonSerializationContext jsc) {
            JsonArray jsonArray = new JsonArray();
            for (Users user : userList) {
                jsonArray.add(usersAdapter.serialize(user, user.getClass(), jsc));
            }
            return jsonArray;
        }

        @Override
        public ArrayList<Users> deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            JsonArray jsonArray = je.getAsJsonArray();
            ArrayList<Users> userArrayList = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                userArrayList.add(usersAdapter.deserialize(element, Users.class, jdc));
            }
            return userArrayList;
        }

    }

    private class UsersAdapter implements JsonSerializer<Users>, JsonDeserializer<Users> {

        private static final String CLASSNAME = "type";
        private static final String INSTANCE = "instance";

        @Override
        public JsonElement serialize(Users user, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            if (user instanceof Employees) {
                jsonObject.addProperty(CLASSNAME, "Employees");
            } else if (user instanceof Customers) {
                jsonObject.addProperty(CLASSNAME, "Customers");
            }
            jsonObject.add(INSTANCE, context.serialize(user));
            return jsonObject;
        }

        @Override
        public Users deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String className = jsonObject.get(CLASSNAME).getAsString();
            JsonElement element = jsonObject.get(INSTANCE);

            if (className.equals("Employees")) {
                return context.deserialize(element, Employees.class);
            } else {
                return context.deserialize(element, Customers.class);
            }
        }

    }

    private class ProductsListAdapter implements JsonSerializer<ArrayList<Products>>, JsonDeserializer<ArrayList<Products>> {

        private final ProductsAdapter productsAdapter = new ProductsAdapter();

        @Override
        public JsonElement serialize(ArrayList<Products> productList, Type type, JsonSerializationContext jsc) {
            JsonArray jsonArray = new JsonArray();
            for (Products prod : productList) {
                jsonArray.add(productsAdapter.serialize(prod, prod.getClass(), jsc));
            }
            return jsonArray;
        }

        @Override
        public ArrayList<Products> deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            JsonArray jsonArray = je.getAsJsonArray();
            ArrayList<Products> productArrayList = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                productArrayList.add(productsAdapter.deserialize(element, Products.class, jdc));
            }
            return productArrayList;
        }

    }

    private class ProductsAdapter implements JsonSerializer<Products>, JsonDeserializer<Products> {

        private static final String CLASSNAME = "type";
        private static final String INSTANCE = "instance";

        @Override
        public JsonElement serialize(Products product, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            if (product instanceof Accessories) {
                jsonObject.addProperty(CLASSNAME, "Accessories");
            } else if (product instanceof Clothing) {
                jsonObject.addProperty(CLASSNAME, "Clothing");
            }
            jsonObject.add(INSTANCE, context.serialize(product));
            return jsonObject;
        }

        @Override
        public Products deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String className = jsonObject.get(CLASSNAME).getAsString();
            JsonElement element = jsonObject.get(INSTANCE);

            if (className.equals("Accessories")) {
                return context.deserialize(element, Accessories.class);
            } else {
                return context.deserialize(element, Clothing.class);
            }

        }

    }

    private class OrdersListAdapter implements JsonSerializer<ArrayList<Orders>>, JsonDeserializer<ArrayList<Orders>> {

        private final OrdersAdapter ordersAdapter = new OrdersAdapter();

        @Override
        public JsonElement serialize(ArrayList<Orders> ordersList, Type type, JsonSerializationContext jsc) {
            JsonArray jsonArray = new JsonArray();
            for (Orders order : ordersList) {
                jsonArray.add(ordersAdapter.serialize(order, order.getClass(), jsc));
            }
            return jsonArray;
        }

        @Override
        public ArrayList<Orders> deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            JsonArray jsonArray = je.getAsJsonArray();
            ArrayList<Orders> productArrayList = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                productArrayList.add(ordersAdapter.deserialize(element, Orders.class, jdc));
            }
            return productArrayList;
        }

    }

    private class OrdersAdapter implements JsonSerializer<Orders>, JsonDeserializer<Orders> {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        @Override
        public JsonElement serialize(Orders order, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            String tempPromo = "NA";
            if (order.getPromoUsed() != null) {
                tempPromo = order.getPromoUsed().getId();
            }
            jsonObject.addProperty("id", order.getId());
            jsonObject.addProperty("user", order.getUser().getId());
            jsonObject.addProperty("status", order.getStatus().name());
            jsonObject.addProperty("address", order.getAddress());
            jsonObject.addProperty("promo", tempPromo);
            jsonObject.addProperty("orderingDate", dateFormat.format(order.getOrderingDate()));

            JsonArray productListsJson = new JsonArray();
            for (Pair<Products, Integer> pair : order.getProductLists()) {
                JsonObject productPair = new JsonObject();
                productPair.addProperty("productId", pair.getKey().getId());
                productPair.addProperty("quantity", pair.getValue());
                productListsJson.add(productPair);
            }
            jsonObject.add("productLists", productListsJson);

            return jsonObject;
        }

        @Override
        public Orders deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String id = jsonObject.get("id").getAsString();
            String userId = jsonObject.get("user").getAsString();
            String statusString = jsonObject.get("status").getAsString();
            String address = jsonObject.get("address").getAsString();
            String orderingDateString = jsonObject.get("orderingDate").getAsString();
            String promoID = jsonObject.get("promo").getAsString();

            JsonArray productListsJson = jsonObject.get("productLists").getAsJsonArray();

            Users particularUser = null;
            for (Users user : userList) {
                if (user.getId().equals(userId)) {
                    particularUser = user;
                }
            }

            Orders.Status status = Orders.Status.valueOf(statusString);

            ArrayList<Pair<Products, Integer>> productLists = new ArrayList<>();
            for (JsonElement element : productListsJson) {
                JsonObject productPair = element.getAsJsonObject();
                String productId = productPair.get("productId").getAsString();
                int quantity = productPair.get("quantity").getAsInt();

                Products product = productList.stream()
                        .filter(p -> p.getId().equals(productId))
                        .findFirst()
                        .orElseThrow(() -> new JsonParseException("Product not found"));

                productLists.add(new Pair<>(product, quantity));

            }

            Date orderingDate = null;
            try {
                orderingDate = dateFormat.parse(orderingDateString);
            } catch (ParseException e) {
                throw new JsonParseException("Failed to parse ordering date", e);
            }
            
            Promo tempPromo = null;
            if (!promoID.equals("NA")) {
                tempPromo = promoList.stream()
                        .filter(p -> p.getId().equals(promoID))
                        .findFirst()
                        .orElseThrow(() -> new JsonParseException("Promo not found"));
            }

            return new Orders(id, address, particularUser, status, productLists, orderingDate, tempPromo);
        }

    }

    private class ProductStockAdapter implements JsonSerializer<HashMap<Products, Integer>>, JsonDeserializer<HashMap<Products, Integer>> {

        private final ProductsAdapter productsAdapter = new ProductsAdapter();

        @Override
        public JsonElement serialize(HashMap<Products, Integer> productStock, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray jsonArray = new JsonArray();
            for (Map.Entry<Products, Integer> entry : productStock.entrySet()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("productId", entry.getKey().getId());
                jsonObject.addProperty("quantity", entry.getValue());
                jsonArray.add(jsonObject);
            }
            return jsonArray;
        }

        @Override
        public HashMap<Products, Integer> deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            HashMap<Products, Integer> productStock = new HashMap<>();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                String productId = jsonObject.get("productId").getAsString();
                int quantity = jsonObject.get("quantity").getAsInt();

                Products product = productList.stream()
                        .filter(p -> p.getId().equals(productId))
                        .findFirst()
                        .orElseThrow(() -> new JsonParseException("Product not found"));
                productStock.put(product, quantity);
            }
            return productStock;
        }
    }

}
