import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

interface PaymentStrategy{
    boolean makePayment(double price, double amount);
}
