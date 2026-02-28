class SpotFactory {
    public static ParkingSpot createSpot(String id, SpotType type) throws IllegalAccessException {
        switch (type){
            case COMPACT:
                return new CompactSpot(id);
            case REGULAR:
                return new RegularSpot(id);
            case OVERSIZED:
                return new LargeSpot(id);
            default:
                throw new IllegalAccessException("Unknown spot type");
        }
    }
}
