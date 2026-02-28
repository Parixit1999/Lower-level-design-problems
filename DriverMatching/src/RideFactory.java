class RideFactory {
    public static Ride createRide(Driver driver, Rider rider, Location sourceLocation, Location destinationLocation) {
        return new Ride(driver, rider, sourceLocation, destinationLocation);
    }
}
