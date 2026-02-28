import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

class RegularSpot extends ParkingSpot {
    public RegularSpot(String id){
        super(id, SpotType.REGULAR);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return vehicle.getType() == VehicleType.MOTORCYCLE || vehicle.getType() == VehicleType.CAR;
    }
}
