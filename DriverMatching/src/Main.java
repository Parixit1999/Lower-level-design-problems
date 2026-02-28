
import java.util.*;
import java.util.concurrent.*;

enum DriverStatus {
    OFFLINE,
    AVAILABLE,
    ON_ROUTE,
    ON_TRIP;
}

class Driver {

    String id;
    Location location;
    DriverStatus driverStatus;

    public Location getLocation() {
        return this.location;
    }

    public DriverStatus getStatus() {
        return this.driverStatus;
    }

    public synchronized boolean updateStatus(DriverStatus driverStatus) {
        if(this.driverStatus != driverStatus) {
            this.driverStatus = driverStatus;
            return true;
        }

        return false;
    }

}

class Rider {

    String id;
    Location location;

    public Location geLocation() {
        return this.location;
    }

}

class Location implements Comparable<Location>{

    long lat;
    long lg;

    @Override
    public int compareTo(Location o) {
        return Long.compare(this.lat, o.lat) + Long.compare(this.lg,  o.lg);
    }
}

static class Ride {

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

class RideFactory {
    public static Ride createRide(Driver driver, Rider rider, Location sourceLocation, Location destinationLocation) {
        return new Ride(driver, rider, sourceLocation, destinationLocation);
    }
}

class SpatialIndex {

    private ConcurrentHashMap<String, CopyOnWriteArrayList<Driver>> driverGeoIndex;

    public SpatialIndex() {
        driverGeoIndex = new ConcurrentHashMap<>();
    }

    public void updateDriverLocation(Driver driver) {
        driverGeoIndex.merge(calculateGeoHash(driver.getLocation()), new CopyOnWriteArrayList<>() , (old_value, new_value) -> {
            new_value.add(driver);
            return new_value;
        });
    }

    private String calculateGeoHash(Location location) {
        return "12345";
    }

    public List<Driver> getDriversByLocation(Location location) {
        return driverGeoIndex.get(this.calculateGeoHash(location));
    }

}

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



void main(String[] args) {



}

