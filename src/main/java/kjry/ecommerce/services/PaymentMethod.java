package kjry.ecommerce.services;

public interface PaymentMethod {
    public boolean makePayment(double total);
}
