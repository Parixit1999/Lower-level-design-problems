import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
