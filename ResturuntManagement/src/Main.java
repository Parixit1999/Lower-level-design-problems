import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
/**
 * The food delivery service should allow customers to browse restaurants, view menus, and place orders.
 Restaurants should be able to manage their menus, prices, and availability.
 Delivery agents should be able to accept and fulfill orders.
 The system should handle order tracking and status updates.
 The system should support multiple payment methods.
 The system should handle concurrent orders and ensure data consistency.
 The system should be scalable and handle a high volume of orders.
 The system should provide real-time notifications to customers, restaurants, and delivery agents.
 */

enum RestaurantStatus{
    OPEN,
    CLOSE
}

enum DeliveryAgentStatus{
    ACTIVE,
    UNACTIVE,
    PROCESSING
}

enum OrderStatus{
    PENDING,
    PREPARING,
    ONWAY,
    DELIVERED
}

class Coordinate{
    long lat;
    long log;

    public Coordinate(long lat, long log){
        this.lat =lat;
        this.log = log;
    }
}

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

interface OrderObserver {
    void onUpdate(String orderId, OrderStatus orderStatus);
}

interface PaymentStrategy{
    long makePayment(Order order, long values);
}

class CashPaymentStrategy implements PaymentStrategy{
    @Override
    public long makePayment(Order order, long values) {
        long change = values - order.getTotal();
        System.out.println("Order value: " + order.getTotal() + " paid:" + values);
        System.out.println("Please collect: " + change);
        return change;
    }
}

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

// We can create singalton pattern to create food items.
class FoodItem {
    String id;
    String name;
    long price;

    public FoodItem(String id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class Menu {

    public List<FoodItem> foodItems;

    public Menu(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    // add or delete food item.

}

class Restaurant {
    public String id;
    public Menu menu;
    public RestaurantStatus status;
    public Coordinate coordinate;

    public Restaurant(Menu menu, Coordinate coordinate) {
        this.id = UUID.randomUUID().toString();
        this.menu = menu;
        this.status = RestaurantStatus.OPEN;
        this.coordinate = coordinate;
    }

    public synchronized void close(){
        this.status = RestaurantStatus.CLOSE;
    }

    public synchronized void open(){
        this.status = RestaurantStatus.OPEN;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Coordinate getCoordinate(){
        return this.coordinate;
    }

}

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



void main(String[] args) {
    PaymentStrategy cashPaymentStrategy = new CashPaymentStrategy();
    Coordinate coordinate = new Coordinate(1, 2);
    Coordinate coordinate2 = new Coordinate(4, 5);
    Coordinate coordinate3 = new Coordinate(7, 6);

    FoodItem foodItem1 = new FoodItem("1", "burito", 10);
    FoodItem foodItem2 = new FoodItem("5", "burito", 5);

    Menu menu = new Menu(Arrays.asList(foodItem1, foodItem2));

    DeliveryAgent agent = new DeliveryAgent("Ramu", "1");
    DeliveryAgent agent2 = new DeliveryAgent("Shamu", "2");

    FoodDeliveryManager manager = new FoodDeliveryManager();


    Restaurant restaurant1 = new Restaurant(menu, coordinate);

    Client client = new Client("Parixit");

    manager.addDriver(agent);
    manager.addDriver(agent2);

    Order order = new Order(client, restaurant1);
    order.addFoodItem(foodItem1, 2);
    order.addFoodItem(foodItem2, 5);

    manager.placeOrder(order, cashPaymentStrategy, 50);
}
