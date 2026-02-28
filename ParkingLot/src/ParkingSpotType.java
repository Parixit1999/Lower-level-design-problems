enum ParkingSpotType{
    COMPACT(5),
    REGULAR(10),
    OVERSIZED(20);

    int size;
    private ParkingSpotType(int size) {
        this.size = size;
    }
}
