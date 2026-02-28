public class DispenseState implements State {
    private final Rack selectedRack;

    public DispenseState(Rack selectedRack) {
        this.selectedRack = selectedRack;
    }

    @Override
    public void Stock(VendingMachine vendingMachine, Product product) {
        System.out.println("Error: please collect the item!");
        vendingMachine.setState(this);
    }

    @Override
    public void Select(VendingMachine vendingMachine, ProductType productType) {
        System.out.println("Error: please collect the item!");
        vendingMachine.setState(this);
    }

    @Override
    public void Payment(VendingMachine vendingMachine, PaymentStrategy paymentStrategy, long coin, long notes) {
        System.out.println("Error: please collect the item!");
        vendingMachine.setState(this);
    }

    @Override
    public void Dispense(VendingMachine vendingMachine) {
        if (this.selectedRack.getFirstProduct().isPresent()) {
            System.out.println("Please collect the item!");
            this.selectedRack.removeProduct();
            vendingMachine.setState(new SelectState());
        } else {
            System.out.println("Product not found, please select it again!");
            vendingMachine.setState(new SelectState());
        }
    }
}
