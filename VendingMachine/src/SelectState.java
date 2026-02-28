public class SelectState implements State {
    @Override
    public void Stock(VendingMachine vendingMachine, Product product) {
        for (Rack rack : vendingMachine.getRacks()) {
            if (rack.getProduceSize() == 0) {
                if (!rack.addProduct(product)) {
                    System.out.println("Cannot find space to add product");
                }
                return;
            } else if (rack.getProductType() == product.getProductType()) {
                if (rack.addProduct(product)) {
                    return;
                }
            }
        }
        System.out.println("Cannot find space to add :" + product.getProductType());
    }

    @Override
    public void Select(VendingMachine vendingMachine, ProductType productType) {
        for (Rack rack : vendingMachine.getRacks()) {
            if (rack.getProductType() == productType) {
                vendingMachine.setState(new PaymentState(rack));
                rack.getFirstProduct().ifPresent(product -> System.out.println("Selected product: " + product.getProductType()));
                return;
            }
        }
        System.out.println("Product not found, please select it again!");
    }

    @Override
    public void Payment(VendingMachine vendingMachine, PaymentStrategy paymentStrategy, long coin, long notes) {
        System.out.println("Error: please select the item from the racks!");
    }

    @Override
    public void Dispense(VendingMachine vendingMachine) {
        System.out.println("Error: please select the item from the racks!");
    }
}
