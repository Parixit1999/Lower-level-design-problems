import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

class VendingMachine {

    private String id;
    private String name;
    private List<Rack> rackList;
    private PaymentStrategy paymentStrategy;

    public VendingMachine(String name,  int numberOfRack, RackFactory rackFactory, PaymentStrategy paymentStrategy) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.rackList = new ArrayList<>();
        this.paymentStrategy = paymentStrategy;
        for(int i = 0; i < numberOfRack; i++) {
            rackList.add(rackFactory.creatRack(i, 10));
        }
        System.out.println(numberOfRack + " racks created for: " + name + " vending machine!");
    }

    public Product getProduct(Rack selectedRack, Product selectedProduct) {
        for(Rack rack: rackList){
            if(Objects.equals(rack.getId(), selectedRack.getId())) {
                for(Product product: rack.getProductList()){
                    if(Objects.equals(product.getId(), selectedProduct.getId())) return product;
                }
            }
        }

        throw new RuntimeException("Didn't find the prodcut!");
    }

    private void removeProduct(Product selectedProduct) {
        for(Rack rack: rackList){
            for(int i = 0; i < rack.getProductList().size(); i ++){
                if(Objects.equals(rack.getProductList().get(i).getId(), selectedProduct.getId())){
                    rack.removeProduct(i);
                    return;
                }
            }
        }

        throw new RuntimeException("Didn't find the prodcut!");
    }

    public boolean addProduct(Rack rack, Product product) {
        for(Rack rack1: rackList) {
            if(rack1.getId().equals(rack.getId())) {
                return rack1.addProduct(product);
            }
        }

        throw new RuntimeException("Didn't find the rack!");
    }

    public List<Product> getProducts (Rack selectedRack) {
        for(Rack rack: rackList){
            if(Objects.equals(rack.getId(), selectedRack.getId())) {
                return rack.getProductList();
            }
        }

        throw new RuntimeException("Didn't find the rack!");
    }

    public boolean processThePayment(Product product, double amount) {

        boolean success = paymentStrategy.makePayment(product.getPrice(), amount);
        if (success) this.removeProduct(product);

        return success;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Rack> getRackList() {
        return rackList;
    }
}
