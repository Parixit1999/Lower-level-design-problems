import java.util.*;
import java.util.concurrent.*;

class ParkingLot{

    private CopyOnWriteArrayList<ParkingSpot> parkingSpots;

    public ParkingLot(CopyOnWriteArrayList<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }

    public Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle) {
        for(ParkingSpot parkingSpot: parkingSpots) {
            if(parkingSpot.parktingSpotType.size >= vehicle.vehicleType.size) {
                return Optional.of(parkingSpot);
            }
        }
        return Optional.empty();
    }

}
