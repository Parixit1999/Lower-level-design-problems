class DriverUpdate{
    Long time;
    Location location;

    public DriverUpdate(Long time, Location location) {
        this.time = time;
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        DriverUpdate compareObject = ((DriverUpdate) obj);

        return compareObject.location.equals(this.location) || compareObject.time.equals(this.time);
    }
}
