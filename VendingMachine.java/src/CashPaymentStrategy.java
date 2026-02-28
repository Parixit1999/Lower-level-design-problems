import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

class CashPaymentStrategy implements  PaymentStrategy {

    public boolean makePayment(double price, double amount) {
        if (amount < price)
        {
            System.out.println("Please pay enough amount");
            return false;
        }

        System.out.println("Please collection the change: $" + calculateChanGe(amount, price));
        System.out.println("Thank you for the payment!");
        return true;
    }

    private double calculateChanGe(double total, double amount) {
        return  total - amount;
    }

}
