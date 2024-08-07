module kjry.ecommerce {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.controlsfx.controls;

    opens kjry.ecommerce to javafx.fxml;
    opens kjry.ecommerce.controller to javafx.fxml,org.controlsfx.controls;
    opens kjry.ecommerce.dataaccess to com.google.gson;
    opens kjry.ecommerce.datamodels to com.google.gson;
    
    exports kjry.ecommerce.dtos;
    exports kjry.ecommerce.controller;
    exports kjry.ecommerce.services;
    exports kjry.ecommerce;
}
