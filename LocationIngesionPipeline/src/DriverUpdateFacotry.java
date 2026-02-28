class DriverUpdateFacotry{
    public static DriverUpdate createDriverUpdate(Long time, Location location) {
        return new DriverUpdate(time, location);
    }
}
