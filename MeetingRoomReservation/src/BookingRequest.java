class BookingRequest implements Comparable {

    private long starTime;
    private long endTime;
    private long capacity;

    public BookingRequest(long startTime, long endTime, long capacity) {
        this.starTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
    }

    public long getStarTime() {
        return starTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getCapacity() {
        return capacity;
    }

    @Override
    public int compareTo(Object o) {
        return Long.compare(this.getStarTime(), ((BookingRequest) o).getStarTime());
    }
}
