import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
/**
 * The food delivery service should allow customers to browse restaurants, view menus, and place orders.

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
