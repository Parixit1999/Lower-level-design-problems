public class CoinNotePaymentStrategy implements PaymentStrategy {
    private final String currency;

    public CoinNotePaymentStrategy(String currency) {
        this.currency = currency;
    }

    @Override
    public long makePayment(long amount, long coin, long notes) {
        if (coin + notes < amount) {
            System.out.println("Not enough notes and coins!");
            return -1;
        }

        long change = coin + notes - amount;
        if (change > 0) {
            System.out.println("Please collect change: " + change + " " + this.currency);
        }

        return amount - coin - notes;
    }
}
