class Ticket{

    Vehicle vehicle;
    ParkingSpot parkingSpot;
    Long startedAt;
    Long endedAt;

    public Ticket(Vehicle vehicle, ParkingSpot parkingSpot) {
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.startedAt = System.currentTimeMillis();
    }

    public Long getTotalChargable(Long currentTime) {

        Long duration = currentTime - startedAt;
        Long totalCharge = duration * VehiclePaymentStategy.DURATION_CHARGE;

        if(vehicle.vehicleType == VehicleType.CAR) {
            totalCharge += VehiclePaymentStategy.CAR_CHARGE;
        } else if(vehicle.vehicleType == VehicleType.MOTORCYCLE) {
            totalCharge += VehiclePaymentStategy.MOTORCYCLE_CHARGE;
        } else{
            totalCharge += VehiclePaymentStategy.TRUCK_CHARGE;
        }

        return totalCharge;
    }
}
