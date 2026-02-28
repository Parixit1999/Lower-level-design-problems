import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

class VehicleFactory {
    public static Vehicle createVehicle(String licensePlate, VehicleType type) {
        switch (type) {
            case CAR:
                return new Car(licensePlate);
            case MOTORCYCLE:
                return new MotorCycle(licensePlate);
            case TRUCK:
                return new Truck(licensePlate);
            default:
                throw new IllegalArgumentException("Unsupported vehicle type");
        }
    }
}
