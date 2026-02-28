import java.util.*;

public class Rack {
    private final String id;
    private final int size;
    private final LinkedList<Product> products;
    private ProductType productType;

    public Rack(String id, int size) {
        this.id = id;
        this.size = size;
        this.products = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public ProductType getProductType() {
        return productType;
    }

    public int getProduceSize() {
        return this.products.size();
    }

    public boolean addProduct(Product product) {
        if (this.products.size() == size) {
            System.out.println("Cannot add more product to this rack!");
            return false;
        }

        if (productType == null) {
            this.productType = product.getProductType();
        }
        this.products.add(product);
        return true;
    }

    public Optional<Product> removeProduct() {
        if (this.products.isEmpty()) {
            System.out.println("Rack is already empty, cannot remove more items!");
            return Optional.empty();
        }
        Product removed = this.products.removeLast();
        System.out.println("Removing a product: " + removed.getProductType());
        return Optional.of(removed);
    }

    public Optional<Product> getFirstProduct() {
        if (this.products.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.products.getFirst());
    }
}
