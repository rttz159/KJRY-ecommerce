package kjry.ecommerce.services;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import kjry.ecommerce.App;

public class OnlineBankingPaymentMethod implements PaymentMethod {

    @Override
    public boolean makePayment(double total) {
        boolean paymentSuccess = false;
        try {
            AnchorPane paymentDialogPane = (AnchorPane) new FXMLLoader(getClass().getResource("/views/OnlineBankingPaymentDialog.fxml")).load();

            TextField accountNumberField = (TextField) paymentDialogPane.lookup("#accountNumberField");
            PasswordField passwordField = (PasswordField) paymentDialogPane.lookup("#passwordField");

            Alert paymentAlert = new Alert(Alert.AlertType.NONE);
            paymentAlert.setTitle("Online Banking Payment Dialog");
            paymentAlert.getDialogPane().setContent(paymentDialogPane);
            paymentAlert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            boolean validation = false;
            while (!validation) {
                Optional<ButtonType> result = paymentAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    String accountNumber = accountNumberField.getText().trim();
                    String password = passwordField.getText().trim();

                    if (accountNumber.isEmpty() || password.isEmpty()) {
                        paymentAlert.close();
                        showConfirmationDialog("Validation Error", "Please enter both account number and password.");
                    } else {
                        paymentSuccess = processPayment(accountNumber, password);
                        if (paymentSuccess) {
                            showConfirmationDialog("Payment Successful", "Your payment has been processed successfully.");
                            validation = true;
                        } else {
                            paymentAlert.close();
                            showConfirmationDialog("Payment Failed", "There was an error processing your payment. Please try again.");
                        }
                    }
                } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
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
        Alert confirmationAlert = new Alert(AlertType.INFORMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText(message);
        confirmationAlert.showAndWait();
    }

    private boolean processPayment(String accountNumber, String password) {
        return true;
    }

}
