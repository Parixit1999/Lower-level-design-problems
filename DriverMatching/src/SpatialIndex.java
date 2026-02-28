import java.util.*;
import java.util.concurrent.*;

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
