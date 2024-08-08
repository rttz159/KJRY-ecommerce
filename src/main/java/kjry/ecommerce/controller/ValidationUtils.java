package kjry.ecommerce.controller;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import javafx.scene.control.Control;

public class ValidationUtils {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PHONE_REGEX = "^\\d{10}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidDate(String date) {
        try {
            new SimpleDateFormat("dd-MM-yyyy").parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean isValidPassword(String password){
        return password != null && !password.trim().isEmpty() && password.length() >= 6;
    }
    
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    public static void setFieldValidity(Control control, boolean isValid) {
        if (isValid) {
            control.getStyleClass().remove("invalid");
        } else {
            control.getStyleClass().add("invalid");
        }
    }
}