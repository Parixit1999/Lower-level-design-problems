
import java.util.*;
import java.util.concurrent.*;

enum VehicleType{
    MOTORCYCLE(5),
    CAR(10),
    TRUCK(20);

    int size;
    private VehicleType(int size) {
        this.size = size;
    }
}
