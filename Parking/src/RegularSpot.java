class RegularSpot extends ParkingSpot {
    public RegularSpot(String id){
        super(id, SpotType.REGULAR);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return vehicle.getType() == VehicleType.MOTORCYCLE || vehicle.getType() == VehicleType.CAR;
    }
}
