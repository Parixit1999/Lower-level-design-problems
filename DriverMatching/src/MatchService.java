import java.util.*;
import java.util.concurrent.*;

class MatchService {

    private SpatialIndex spatialIndex;

    public MatchService(SpatialIndex spatialIndex) {
        this.spatialIndex = spatialIndex;
    }

    public Optional<Ride> requestRide(Rider rider, Location source, Location desLocation) {
        List<Driver> drivers = this.spatialIndex.getDriversByLocation(source);
        PriorityBlockingQueue<Driver> closestDrivers = new PriorityBlockingQueue<>(drivers);

        while(!closestDrivers.isEmpty()) {
            Driver closestDriver = closestDrivers.poll();
            synchronized(closestDriver) {
                if(closestDriver.getStatus() == DriverStatus.AVAILABLE){
                    return Optional.of(RideFactory.createRide(closestDriver, rider, source, desLocation));
                }
            }
        }

        return Optional.empty();
    }
}
