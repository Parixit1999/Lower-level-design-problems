import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
