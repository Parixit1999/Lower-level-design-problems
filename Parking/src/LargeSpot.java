import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

class LargeSpot extends ParkingSpot {
    public LargeSpot(String id){
        super(id, SpotType.OVERSIZED);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return true;
    }
}
