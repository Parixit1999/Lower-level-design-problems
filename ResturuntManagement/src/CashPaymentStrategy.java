import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
/**
 * The food delivery service should allow customers to browse restaurants, view menus, and place orders.

class CashPaymentStrategy implements PaymentStrategy{
    @Override
    public long makePayment(Order order, long values) {
        long change = values - order.getTotal();
        System.out.println("Order value: " + order.getTotal() + " paid:" + values);
        System.out.println("Please collect: " + change);
        return change;
    }
}
