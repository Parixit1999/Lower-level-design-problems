import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
/**
 * The food delivery service should allow customers to browse restaurants, view menus, and place orders.

public class Main {
public static void main(String[] args) {
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
}
