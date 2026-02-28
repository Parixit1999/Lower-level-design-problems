//
//import java.util.*;
//
//// --- Core Models ---
//enum ProductType { BEVERAGES, SNACKS, MILK, DRINK }
//enum UserRole { ADMIN_ROLE, USER_ROLE }
//
//class Product {
//    private final String description;
//    private final double price;
//    public Product(String description, double price) {
//        this.description = description;
//        this.price = price;
//    }
//    public String getDescription() { return description; }
//    public double getPrice() { return price; }
//}
//
//// --- State Pattern Interface ---
//interface VendingState {
//    void insertMoney(VendingMachine machine, double amount);
//    void selectProduct(VendingMachine machine, String code);
//    void dispense(VendingMachine machine);
//}
//
//// --- Vending Machine (The Context) ---
//class VendingMachine {
//    private VendingState currentState;
//    private double currentBalance;
//    private String selectedCode;
//    private final Map<String, Stack<Product>> inventory = new HashMap<>(); // Code -> Stack of items
//    private final PaymentStrategy paymentStrategy;
//
//    public VendingMachine(PaymentStrategy strategy) {
//        this.paymentStrategy = strategy;
//        this.currentState = new IdleState();
//        this.currentBalance = 0;
//    }
//
//    // State Delegation
//    public void insertMoney(double amount) { currentState.insertMoney(this, amount); }
//    public void selectProduct(String code) { currentState.selectProduct(this, code); }
//    public void dispense() { currentState.dispense(this); }
//
//    // Admin Helpers
//    public void addProduct(String code, Product product, int count) {
//        inventory.putIfAbsent(code, new Stack<>());
//        for(int i = 0; i < count; i++) inventory.get(code).push(product);
//    }
//
//    // Getters/Setters
//    protected void setState(VendingState state) { this.currentState = state; }
//    protected double getBalance() { return currentBalance; }
//    protected void setBalance(double balance) { this.currentBalance = balance; }
//    protected String getSelectedCode() { return selectedCode; }
//    protected void setSelectedCode(String code) { this.selectedCode = code; }
//    protected Map<String, Stack<Product>> getInventory() { return inventory; }
//    protected PaymentStrategy getPaymentStrategy() { return paymentStrategy; }
//}
//
//// --- Concrete States ---
//class IdleState implements VendingState {
//    public void insertMoney(VendingMachine vm, double amount) {
//        vm.setBalance(amount);
//        System.out.println("$" + amount + " inserted.");
//        vm.setState(new ReadyState());
//    }
//    public void selectProduct(VendingMachine vm, String code) { System.out.println("Insert money first!"); }
//    public void dispense(VendingMachine vm) { System.out.println("Nothing selected."); }
//}
//
//class ReadyState implements VendingState {
//    public void insertMoney(VendingMachine vm, double amount) {
//        vm.setBalance(vm.getBalance() + amount);
//        System.out.println("Balance updated: $" + vm.getBalance());
//    }
//    public void selectProduct(VendingMachine vm, String code) {
//        Stack<Product> rack = vm.getInventory().get(code);
//        if (rack == null || rack.isEmpty()) {
//            System.out.println("Product " + code + " is Out of Stock!");
//            return;
//        }
//        Product p = rack.peek();
//        if (vm.getBalance() >= p.getPrice()) {
//            vm.setSelectedCode(code);
//            vm.setState(new DispensingState());
//            vm.dispense(); // Auto-transition
//        } else {
//            System.out.println("Insufficient funds for " + p.getDescription() + " ($" + p.getPrice() + ")");
//        }
//    }
//    public void dispense(VendingMachine vm) { System.out.println("Select a product."); }
//}
//
//class DispensingState implements VendingState {
//    public void insertMoney(VendingMachine vm, double amount) { System.out.println("Please wait, dispensing..."); }
//    public void selectProduct(VendingMachine vm, String code) { System.out.println("Processing current selection..."); }
//    public void dispense(VendingMachine vm) {
//        Product p = vm.getInventory().get(vm.getSelectedCode()).pop();
//        vm.getPaymentStrategy().makePayment(p.getPrice(), vm.getBalance());
//        vm.setBalance(0);
//        vm.setSelectedCode(null);
//        vm.setState(new IdleState());
//    }
//}
//
//// --- Payment Strategy ---
//interface PaymentStrategy { void makePayment(double price, double amount); }
//class CashPaymentStrategy implements PaymentStrategy {
//    public void makePayment(double price, double amount) {
//        System.out.println("Dispensing item. Change returned: $" + (amount - price));
//    }
//}
//
//// --- Facades ---
//class UserFacade {
//    private final VendingMachine vm;
//    public UserFacade(VendingMachine vm) { this.vm = vm; }
//    public void buyProduct(String code, double money) {
//        vm.insertMoney(money);
//        vm.selectProduct(code);
//    }
//}
//
//class AdminFacade {
//    private final VendingMachine vm;
//    public AdminFacade(VendingMachine vm) { this.vm = vm; }
//    public void restock(String code, Product product, int count) {
//        vm.addProduct(code, product, count);
//        System.out.println("Restocked " + code + " with " + count + " items.");
//    }
//}
//
//
