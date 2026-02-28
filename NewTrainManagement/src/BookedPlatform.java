class BookedPlatform implements Comparable<BookedPlatform>{
    String platformId;
    String trainId;
    long departureTime;
    long arrivalTime;

    public BookedPlatform (String platformId, String trainId, long arrivalTime, long departureTime) {
        this.platformId = platformId;
        this.trainId = trainId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    @Override
    public int compareTo(BookedPlatform o) {
        int result = Long.compare(this.departureTime, o.departureTime);
        if(result == 0) {
            return this.platformId.compareTo(o.platformId);
        }

        return result;
    }
}
