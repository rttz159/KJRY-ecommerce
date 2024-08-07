package kjry.ecommerce.services;

public class PaymentService {
    private PaymentMethod paymentMethod;
    
    public PaymentService(PaymentMethod method){
        paymentMethod = method;
    }
    
    public boolean makePayment(Double total){
        return paymentMethod.makePayment(total);
    }
}
