abstract class Vehicle {
    private final String licencePlate;
    private final VehicleType type;

    public Vehicle(String licencePlate, VehicleType type) {
        this.licencePlate = licencePlate;
        this.type = type;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public VehicleType getType() {
        return type;
    }
}
