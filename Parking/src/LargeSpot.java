class LargeSpot extends ParkingSpot {
    public LargeSpot(String id){
        super(id, SpotType.OVERSIZED);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return true;
    }
}
