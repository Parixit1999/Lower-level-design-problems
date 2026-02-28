import java.util.*;

class PaymentProcessor {
    private final Stack<PaymentCommand> history = new Stack<>();

    public boolean process(PaymentCommand command) {
        if (command.execute()) {
            history.push(command);
            return true;
        }
        return false;
    }

    public void refundLast() {
        if (!history.isEmpty()) {
            history.pop().undo();
        }
    }
}
