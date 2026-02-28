import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
/**
 * The food delivery service should allow customers to browse restaurants, view menus, and place orders.

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
