import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
/**
 * The food delivery service should allow customers to browse restaurants, view menus, and place orders.

class DeliveryAgent implements OrderObserver {
    String id;
    String name;
    Coordinate cord;
    DeliveryAgentStatus status;

    public DeliveryAgent(String name, String id) {
        this.id = id;
        this.name = name;
        cord = null;
        this.status = DeliveryAgentStatus.ACTIVE;
    }


    public synchronized void updateCoordinate(Coordinate updatedCord) {
        this.cord = updatedCord;
    }

    public synchronized boolean orderRequest(Order order) {
        boolean accept = DeliveryAgentStatus.ACTIVE == this.status; // accept order
        if(accept) {
            this.status = DeliveryAgentStatus.PROCESSING;
        }
        return accept;
    }

    @Override
    public void onUpdate(String orderId, OrderStatus orderStatus) {
        System.out.print("Agent: Order: " + orderId + " status changed to: " + orderStatus);
    }

}
