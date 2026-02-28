import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

class CashPaymentCommand implements PaymentCommand {
    private double amount;

    public CashPaymentCommand(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean execute() {
        System.out.println("[Machine] Verifying cash payment of $" + amount);
        return true;
    }

    @Override
    public void undo() {
        System.out.println("[Machine] Dispensing cash refund of $" + amount);
    }
}
