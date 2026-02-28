import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

class PaymentCommandFactory {
    // We use a "detail" only for types that need it (Card, UPI)
    public static PaymentCommand createCommand(PaymentType type, double amount, String detail) {
        switch (type) {
            case CARD:
                return new CardPaymentCommand(amount, detail);
            case CASH:
                return new CashPaymentCommand(amount); // Detail is ignored here
            default:
                throw new IllegalArgumentException("Unsupported Payment Type");
        }
    }
}
