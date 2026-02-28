public class PaymentState implements State {
    private final Rack selectedRack;

    public PaymentState(Rack selectedRack) {
        this.selectedRack = selectedRack;
    }

    @Override
    public void Stock(VendingMachine vendingMachine, Product product) {
        System.out.println("Error: please make a payment!");
    }

    @Override
    public void Select(VendingMachine vendingMachine, ProductType productType) {
        System.out.println("Error: please make a payment!");
    }

    @Override
    public void Payment(VendingMachine vendingMachine, PaymentStrategy paymentStrategy, long coin, long notes) {
        long status = this.selectedRack
                .getFirstProduct()
                .map(product -> paymentStrategy.makePayment(product.getPrice(), coin, notes))
                .orElse(-1L);

        if (status == -1) {
            System.out.println("Cancelling the payment, please select the item again!");
            vendingMachine.setState(new SelectState());
        } else {
            vendingMachine.setState(new DispenseState(selectedRack));
            vendingMachine.dispenseProduct();
        }
    }

    @Override
    public void Dispense(VendingMachine vendingMachine) {
        System.out.println("Error: please make a payment!");
    }
}
