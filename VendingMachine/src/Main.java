import lombok.Getter;

public enum Role {
    USER,
    ADMIN
}

public enum ProductType{
    COCA_COLA(10),
    SNAKE(5),
    WATER(10),
    SOFT_DRINK(5),
    COCONUT_WATER(10);

    public int price;
    ProductType(int price) {
        this.price = price;
    }
}

public class Person{

    private String name;
    private Role role;

    public Person(String name, Role role) {
        this.name = name;
        this.role = role;
    }
}

public class Admin extends Person{
    private Role roles;

    public Admin(String name) {
        super(name, Role.ADMIN);
    }
}

public class User extends Person{
    private Role roles;

    public User(String name) {
        super(name, Role.USER);
    }
}

public class Rack {
    private String id;
    @Getter
    private int size;
    private LinkedList<Product> products;
    @Getter
    private ProductType productType;

    public Rack(String id, int size) {
        this.id = id;
        this.size = size;
        this.products = new LinkedList<>();
    }

    public int getProduceSize() {
        return this.products.size();
    }

    public boolean addProduct(Product product) {
        if(this.products.size() == size) {
            System.out.println("Cannot add more product to this rack!");
            return false;
        }

        if(productType == null) {
            this.productType = product.getProductType();
        }
        this.products.add(product);
        return true;
    }

    public Optional<Product> removeProduct() {
        if(this.products.size() == 0) {
            System.out.println("Rack is already empty, cannot remove more items!");
            return Optional.empty();
        }
        System.out.println("Removing a product: " +  this.products.getLast().productType);
        return Optional.of(this.products.removeLast());
    }

    public Optional<Product> getFirstProduct() {
       return Optional.of(this.products.getFirst());
    }
}


public class Product{

    private String id;
    private ProductType productType;

    public Product(String id, ProductType productType) {
        this.id = id;
        this.productType = productType;
    }

    public int getPrice() {
        return this.productType.price;
    }

    public ProductType getProductType() {
        return this.productType;
    }
}

interface State {
    void Stock(VendingMachine v, Product product);
    void Select(VendingMachine v, ProductType productType);
    void Payment(VendingMachine v, PaymentStrategy paymentStrategy, long coin, long notes);
    void Dispense(VendingMachine v);
}

class SelectState implements State {

    @Override
    public void Stock(VendingMachine v, Product product) {
        for(Rack rack: v.getRacks()) {
            if(rack.getProduceSize() == 0) {
//                System.out.println(product.getProductType());
                if(!rack.addProduct(product)) {
                    System.out.println("Cannot find space to add product");
                }
                return;
            } else{
                if(rack.getProductType() == product.getProductType()) {
                    if(rack.addProduct(product)) {
                        return;
                    }
                }
            }
        }
        System.out.println("Cannot find space to add :" +  product.productType);
    }

    @Override
    public void Select(VendingMachine v, ProductType productType) {
        for(Rack rack: v.getRacks()) {
            if(rack.getProductType() == productType) {
                v.setState(new PaymentState(rack));
                System.out.println("Selected product: " + rack.getFirstProduct().get().getProductType());
            }
        }

    }

    @Override
    public void Payment(VendingMachine v, PaymentStrategy paymentStrategy, long coin, long notes) {
        System.out.println("Error: please select the item from the racks!");
    }


    @Override
    public void Dispense(VendingMachine v) {
        System.out.println("Error: please select the item from the racks!");
    }
}

class PaymentState implements State {
    private Rack selectedRack;

    public PaymentState(Rack selectedRack) {
        this.selectedRack = selectedRack;
    }

    @Override
    public void Stock(VendingMachine v, Product product) {
        System.out.println("Error: please make a payment!");
    }

    @Override
    public void Select(VendingMachine v, ProductType productType) {
        System.out.println("Error: please make a payment!");
    }

    @Override
    public void Payment(VendingMachine v, PaymentStrategy paymentStrategy, long coin, long notes) {
        var selectedProduct = this.selectedRack.getFirstProduct();
        var status = selectedProduct.map(product -> paymentStrategy.makePayment(product.getPrice(), coin, notes)).orElse(-1L);

        if(status == -1) {
            System.out.println("Cancelling the payment, please select the item again!");
            v.setState(new SelectState());
        } else {
            v.setState(new DispenseState(selectedRack));
            v.dispenseProduct();
        }
    }

    @Override
    public void Dispense(VendingMachine v) {
        System.out.println("Error: please make a payment!");
    }
}

class DispenseState implements State {

    private Rack selectedRack;

    public DispenseState(Rack selectedRack) {
        this.selectedRack = selectedRack;
    }

    @Override
    public void Stock(VendingMachine v, Product product) {
        System.out.println("Error: please collect the item!");
        v.setState(this);
    }

    @Override
    public void Select(VendingMachine v, ProductType productType) {
        System.out.println("Error: please collect the item!");
        v.setState(this);
    }

    @Override
    public void Payment(VendingMachine v, PaymentStrategy paymentStrategy, long coin, long notes) {
        System.out.println("Error: please collect the item!");
        v.setState(this);
    }

    @Override
    public void Dispense(VendingMachine v) {
        var selectedProduct = this.selectedRack.getFirstProduct();

        if(selectedProduct.isPresent()){
            System.out.println("Please collect the item!");
            selectedRack.removeProduct();
            v.setState(new SelectState());
        } else{
            System.out.println("Product not found, please select it again!");
            v.setState(new SelectState());
        }

    }
}


public class VendingMachine {

    private State currentState;
    private List<Rack> racks;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public VendingMachine(List<Rack> racks) {
        this.racks = racks;
        this.currentState = new SelectState();
    }

    public void setState(State state) {
        this.currentState = state;
    }

    public List<Rack> getRacks() {
        return this.racks;
    }

    public Map<ProductType, Integer> getProductList(){
        Map<ProductType, Integer> ans = new HashMap<>();

        for(Rack rack: racks) {
            ans.put(rack.getProductType(), rack.getProduceSize());
        }
        return ans;
    }

    public synchronized void addProduct(Product product) {
        this.currentState.Stock(this, product);
    }

    private synchronized void selectProduct(ProductType productType) {
        this.currentState.Select(this, productType);
    }

    public synchronized void makePayment(PaymentStrategy paymentStrategy, long coin, long notes) {
        this.currentState.Payment(this, paymentStrategy, coin, notes);
    }

    public synchronized void dispenseProduct() {
        this.currentState.Dispense(this);
    }
}

interface PaymentStrategy {
    long makePayment(long amount, long coin, long notes);
}

class CoinNotePaymentStrategy implements  PaymentStrategy {

    private String currency;

    public CoinNotePaymentStrategy(String currency) {
        this.currency = currency;
    }

    @Override
    public long makePayment(long amount, long coin, long notes) {

        if(coin + notes < amount) {
            System.out.println("Not enough notes and coins!");
            return -1;
        }

        long change = coin + notes - amount;
        if(change > 0) {
            System.out.println("Please collect change: " + change + " " + this.currency);
        }

        return amount - coin - notes;
    }
}


void main() {
    CoinNotePaymentStrategy paymentStrategy = new CoinNotePaymentStrategy("$");
    Rack rack1 = new Rack("A1", 10);
    Rack rack2 = new Rack("A2", 10);
    Rack rack3 = new Rack("A3", 10);
    Rack rack4 = new Rack("A4", 10);

    VendingMachine v = new VendingMachine(Arrays.asList(rack1, rack2, rack3, rack4));

    for(ProductType productType: ProductType.values()) {
        for(int i = 0; i < 8; i++){
            v.addProduct(new Product(String.valueOf(i), productType));
        }
    }

    System.out.println(v.getProductList());

    v.selectProduct(ProductType.COCA_COLA);
    v.makePayment(paymentStrategy, 10, 0);

    System.out.println(v.getProductList());


}
