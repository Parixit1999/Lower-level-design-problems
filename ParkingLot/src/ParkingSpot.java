class ParkingSpot{

    String id;
    ParkingSpotType parktingSpotType;
    ParkingSpotStatus parkingSpotStatus;

    public ParkingSpot(String id, ParkingSpotType parkingSoptType) {
        this.id = id;
        this.parktingSpotType = parkingSoptType;
    }

    public synchronized void updateParkingSpotStatus(ParkingSpotStatus newStatus) {
        this.parkingSpotStatus = newStatus;
    }

}
