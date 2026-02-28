import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
/**
 * The food delivery service should allow customers to browse restaurants, view menus, and place orders.

class Order{
    String id;
    OrderStatus status;
    Restaurant restaurant;
    DeliveryAgent agent;
    Client client;
    ConcurrentHashMap<FoodItem, Integer> foodItems;
    List<OrderObserver> observers = new ArrayList<>();


    // I could have state pattern for the order, defining behavior based on the state of the order;

    public Order(Client client, Restaurant restaurant) {
        this.id = UUID.randomUUID().toString();
        this.restaurant =restaurant;
        this.foodItems = new ConcurrentHashMap<>();
        this.client = client;
        observers.add(client);
    }

    public synchronized void assignDeliveryAgent(DeliveryAgent agent) {
        this.agent = agent;
        observers.add(agent);
        this.setStatus(OrderStatus.ONWAY);
    }

    public synchronized void setStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
        this.notifyObserver();
    }

    private void notifyObserver(){
        for(OrderObserver observer: observers) {
            observer.onUpdate(this.id, this.status);
        }
    }

    public synchronized void addFoodItem(FoodItem foodItem, int count) {
        foodItems.merge(foodItem, count, (old_val, new_val) -> new_val + count);
    }

    public long getTotal() {
        long total = 0;
        for(FoodItem food: foodItems.keySet()) {
            // System.out.println(food.price + " " + foodItems.get(food));
            total += food.price * foodItems.get(food);
        }
        return total;
    }
}
