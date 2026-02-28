import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        CoinNotePaymentStrategy paymentStrategy = new CoinNotePaymentStrategy("$");
        Rack rack1 = new Rack("A1", 10);
        Rack rack2 = new Rack("A2", 10);
        Rack rack3 = new Rack("A3", 10);
        Rack rack4 = new Rack("A4", 10);

        VendingMachine vendingMachine = new VendingMachine(Arrays.asList(rack1, rack2, rack3, rack4));

        for (ProductType productType : ProductType.values()) {
            for (int i = 0; i < 8; i++) {
                vendingMachine.addProduct(new Product(String.valueOf(i), productType));
            }
        }

        System.out.println(vendingMachine.getProductList());
        vendingMachine.selectProduct(ProductType.COCA_COLA);
        vendingMachine.makePayment(paymentStrategy, 10, 0);
        System.out.println(vendingMachine.getProductList());
    }
}
