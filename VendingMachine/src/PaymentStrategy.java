public interface PaymentStrategy {
    long makePayment(long amount, long coin, long notes);
}
