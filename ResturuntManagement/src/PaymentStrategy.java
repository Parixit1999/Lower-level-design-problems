interface PaymentStrategy{
    long makePayment(Order order, long values);
}
