import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
