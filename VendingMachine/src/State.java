public interface State {
    void Stock(VendingMachine vendingMachine, Product product);

    void Select(VendingMachine vendingMachine, ProductType productType);

    void Payment(VendingMachine vendingMachine, PaymentStrategy paymentStrategy, long coin, long notes);

    void Dispense(VendingMachine vendingMachine);
}
