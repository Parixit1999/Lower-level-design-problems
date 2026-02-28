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
