class CompactSpot extends ParkingSpot {
    public CompactSpot(String id){
        super(id, SpotType.COMPACT);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return vehicle.getType() == VehicleType.MOTORCYCLE;
    }
}
