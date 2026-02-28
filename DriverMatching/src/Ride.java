
import java.util.*;
import java.util.concurrent.*;

class Ride {

    Driver driver;
    Rider rider;
    Location sourceLocation;
    Location destinaLocation;

    public Ride(Driver driver, Rider rider, Location sourceLocation, Location destinaLocation) {
        this.driver = driver;
        this.rider = rider;
        this.sourceLocation = sourceLocation;
        this.destinaLocation = destinaLocation;
    }
}
