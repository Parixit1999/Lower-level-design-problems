
import java.util.*;
import java.util.concurrent.*;

class Driver{
    String id;
    DriverStatus driverStatus;

    public Driver(String id) {
        this.id = id;
        this.driverStatus = DriverStatus.AVAILABLE;
    }
}
