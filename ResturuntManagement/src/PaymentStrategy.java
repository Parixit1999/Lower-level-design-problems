import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
/**
 * The food delivery service should allow customers to browse restaurants, view menus, and place orders.

interface PaymentStrategy{
    long makePayment(Order order, long values);
}
