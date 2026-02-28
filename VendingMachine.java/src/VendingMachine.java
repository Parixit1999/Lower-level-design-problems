import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

enum ProductType {
    BEVERAGES,
    SNACKS,
    MILK,
    DRINK
}

enum UserRole{
    ADMIN_ROLE,
    USER_ROLE
}

abstract class Person {
    private String id;
    private String name;
    private UserRole role;

    public Person(String id, String name, UserRole role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }
}

class User extends Person {
    public User(String id, String name) {
        super(id, name, UserRole.USER_ROLE);
    }

}

class Admin extends Person {
    public Admin(String id, String name) {
        super(id, name, UserRole.ADMIN_ROLE);
    }
}



class Product {

    private String id;
    private String description;
    private double price;
    private ProductType productType;

    public Product(ProductType productType, String description, double price) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.price = price;
        this.productType = productType;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}

class Rack {

    private String id;
    private String name;
    private long size;
    private List<Product> productList = new ArrayList<>();

    public Rack(String name, long size ) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.size = size;
    }

    public boolean addProduct(Product product) {
        if(this.size > productList.size()) {
            this.productList.add(product);
            return true;
        }

        System.out.println("Rack is full!");
        return false;
    }

    public  void removeProduct(int productIdx) {
        this.productList.remove(productIdx);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public long getSize() {
        return size;
    }
}

class UserFacade {
    private VendingMachine vendingMachine;
    private Product selectedProduct;

    public UserFacade(VendingMachine vendingMachine, UserRole userRole) {
        if(userRole != UserRole.USER_ROLE) {
            throw new RuntimeException("This class is for the user only!");
        }
        this.vendingMachine = vendingMachine;
        this.selectedProduct = null;
    }

    public void selectProduct(Product product) {
        System.out.println(product.getId() + " gets selected!");
        System.out.println("Make a payment of $:"+ product.getPrice() + " for the purchase!");
        selectedProduct = product;
    }

    public void unSelectProduct(Product product) {
        selectedProduct = null;
    }

    public boolean makePayment(double amount) {
        if(selectedProduct == null) {
            throw new RuntimeException("Please select the product first!");
        }

        if(vendingMachine.processThePayment(selectedProduct, amount)) {
            selectedProduct = null;
            return true;
        }

        return false;
    }

    public List<Product> getProducts(Rack rack) {
        return this.vendingMachine.getProducts(rack);
    }
}

class AdminFacade {
    private VendingMachine vendingMachine;

    public AdminFacade(VendingMachine vendingMachine, UserRole userRole) {
        if(userRole != UserRole.ADMIN_ROLE) {
            throw new RuntimeException("This class is for the admin only!");
        }
        this.vendingMachine = vendingMachine;
    }

    public boolean addProduct(Rack rack, Product product) {
        return this.vendingMachine.addProduct(rack, product);
    }
}

class RackFactory{
    private int numberOfProducts = 10;

    public Rack creatRack(int rackNumber , int size) {
        this.numberOfProducts = size;
        return new Rack(String.valueOf((char) rackNumber + 97), numberOfProducts);
    }
}



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


interface PaymentStrategy{
    boolean makePayment(double price, double amount);
}

class CashPaymentStrategy implements  PaymentStrategy {

    public boolean makePayment(double price, double amount) {
        if (amount < price)
        {
            System.out.println("Please pay enough amount");
            return false;
        }

        System.out.println("Please collection the change: $" + calculateChanGe(amount, price));
        System.out.println("Thank you for the payment!");
        return true;
    }

    private double calculateChanGe(double total, double amount) {
        return  total - amount;
    }

}