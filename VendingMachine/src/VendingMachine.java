import java.util.*;
import java.util.concurrent.locks.*;

public class VendingMachine {
    private State currentState;
    private final List<Rack> racks;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

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

    public Map<ProductType, Integer> getProductList() {
        Map<ProductType, Integer> products = new HashMap<>();
        for (Rack rack : racks) {
            products.put(rack.getProductType(), rack.getProduceSize());
        }
        return products;
    }

    public synchronized void addProduct(Product product) {
        lock.writeLock().lock();
        try {
            this.currentState.Stock(this, product);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public synchronized void selectProduct(ProductType productType) {
        lock.writeLock().lock();
        try {
            this.currentState.Select(this, productType);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public synchronized void makePayment(PaymentStrategy paymentStrategy, long coin, long notes) {
        lock.writeLock().lock();
        try {
            this.currentState.Payment(this, paymentStrategy, coin, notes);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public synchronized void dispenseProduct() {
        lock.writeLock().lock();
        try {
            this.currentState.Dispense(this);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
