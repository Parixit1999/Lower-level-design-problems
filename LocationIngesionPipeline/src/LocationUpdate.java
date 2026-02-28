
import java.util.*;
import java.util.concurrent.*;

class LocationUpdate extends Message{
    Driver driver;
    Location location;
    long time;

    public LocationUpdate(Driver driver, Location location, long time) {
        super();
        this.driver = driver;
        this.location = location;
        this.time = time;
    }
}
