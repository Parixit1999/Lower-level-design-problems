import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

class CardPaymentCommand implements PaymentCommand{

    private double amount;
    private String cardNumber;

    public CardPaymentCommand(double amount, String cardNumber){
        this.amount = amount;
        this.cardNumber = cardNumber;
    }

    public boolean execute(){
        System.out.println("[Bank API] Processing $" + amount + " from card " + cardNumber);
        return true;
    }

    @Override
    public void undo() {
        System.out.println("[Bank API] Refunding $" + amount + " to card " + cardNumber);
    }
}
