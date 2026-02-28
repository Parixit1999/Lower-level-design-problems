import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
/**
 * The food delivery service should allow customers to browse restaurants, view menus, and place orders.

class Client implements OrderObserver {
    String name;

    public Client(String name) {
        this.name = name;
    }

    @Override
    public void onUpdate(String orderId, OrderStatus orderStatus) {

        System.out.println("Client: Order: " + orderId + " status changed to: " + orderStatus);
    }
}
