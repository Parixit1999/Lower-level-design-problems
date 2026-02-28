import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

class FoodDeliveryManager {

    public ConcurrentHashMap<String, Restaurant> restaurantMap;
    public ConcurrentHashMap<String , DeliveryAgent> driverHashMap;

    public FoodDeliveryManager() {
        this.restaurantMap = new ConcurrentHashMap<>();
        this.driverHashMap = new ConcurrentHashMap<>();
    }

    // Add restaurent
    public void addRestaurant(Restaurant restaurant) {
        this.restaurantMap.putIfAbsent(restaurant.id, restaurant);
    }

    public void addDriver(DeliveryAgent agent) {
        this.driverHashMap.putIfAbsent(agent.id, agent);
    }

    // Add driver

    public List<Restaurant> viewRestaurants() {
        return restaurantMap.values().stream().collect(Collectors.toList());
    }

    public Menu viewMenu(String restaurantId) {
        return this.restaurantMap.get(restaurantId).getMenu();
    }

    public boolean placeOrder(Order order, PaymentStrategy paymentStrategy, long values) {

        long status = paymentStrategy.makePayment(order, values);
        if(status >= 0) {
            for(DeliveryAgent agent: driverHashMap.values()){
                synchronized(agent){
                    if(agent.status == DeliveryAgentStatus.ACTIVE) {
                        if(agent.orderRequest(order)){

                            order.assignDeliveryAgent(agent);

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
