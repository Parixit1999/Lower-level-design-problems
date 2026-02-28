import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
