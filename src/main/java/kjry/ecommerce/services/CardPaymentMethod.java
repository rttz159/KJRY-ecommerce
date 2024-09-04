package kjry.ecommerce.services;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kjry.ecommerce.App;

public class CardPaymentMethod implements PaymentMethod {

    @Override
    public boolean makePayment(double total) {
        boolean paymentSuccess = false;
        try {
            AnchorPane paymentDialogPane = (AnchorPane) new FXMLLoader(getClass().getResource("/views/CardPaymentDialog.fxml")).load();

            TextField cardNumberField = (TextField) paymentDialogPane.lookup("#cardNumberField");
            TextField csvField = (TextField) paymentDialogPane.lookup("#csvField");
            PasswordField pinField = (PasswordField) paymentDialogPane.lookup("#pinField");

            Alert paymentAlert = new Alert(Alert.AlertType.NONE);
            paymentAlert.setTitle("Card Payment Dialog");
            paymentAlert.getDialogPane().setContent(paymentDialogPane);
            paymentAlert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            boolean validation = false;
            while (!validation) {
                Optional<ButtonType> result = paymentAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    String cardNumber = cardNumberField.getText().trim();
                    String csv = csvField.getText().trim();
                    String pin = pinField.getText().trim();

                    if (cardNumber.isEmpty() || pin.isEmpty() || csv.isEmpty()) {
                        paymentAlert.close();
                        showConfirmationDialog("Validation Error", "Please enter all the fields.");
                    } else if (csv.length() != 3) {
                        paymentAlert.close();
                        showConfirmationDialog("Validation Error", "Please enter valid CSV.");
                    } else if (!pin.matches("\\d{6}")) {
                        paymentAlert.close();
                        showConfirmationDialog("Validation Error", "Please enter valid pin.");
                    }else{
                        paymentSuccess = processPayment(cardNumber, csv, pin);
                        if (paymentSuccess) {
                            paymentAlert.close();
                            showConfirmationDialog("Payment Successful", "Your payment has been processed successfully.");
                            validation = true;
                        } else {
                            paymentAlert.close();
                            showConfirmationDialog("Payment Failed", "There was an error processing your payment. Please try again.");
                        }
                    }
                } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                    paymentAlert.close();
                    showConfirmationDialog("Payment Cancelled", "There will be no changes made.");
                    break;
                }
            }
        } catch (IOException ex) {
            showConfirmationDialog("Payment Failed", "There was an error when opening the payment page. Please contact the developer.");
        }
        return paymentSuccess;
    }

    private void showConfirmationDialog(String title, String message) {
        Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText(message);
        confirmationAlert.showAndWait();
    }

    private boolean processPayment(String cardNumber, String csv, String pin) {
        return true;
    }

}
