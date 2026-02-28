public class Product {
    private final String id;
    private final ProductType productType;

    public Product(String id, ProductType productType) {
        this.id = id;
        this.productType = productType;
    }

    public String getId() {
        return id;
    }

    public int getPrice() {
        return this.productType.getPrice();
    }

    public ProductType getProductType() {
        return this.productType;
    }
}
