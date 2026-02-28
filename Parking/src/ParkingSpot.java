import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

abstract class ParkingSpot{

    private String id;
    private SpotType type;
    private boolean isFree = true;
    private Vehicle parkedVehicle;

    public ParkingSpot(String id, SpotType type) {
        this.id = id;
        this.type = type;
    }

    public abstract boolean canFitVehicle(Vehicle vehicle);

    public void park(Vehicle v) {
        this.parkedVehicle = v;
        this.isFree = false;
    }

    public void unpark() {
        this.parkedVehicle = null;
        this.isFree = true;
    }

    public boolean isFree() {
        return isFree;
    }

    public SpotType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }
}
