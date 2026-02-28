class BookedPlatformFactory{
    public BookedPlatform bookPlatform(String platformId, String trainId, long arrivalTime, long departureTime) {
        return new BookedPlatform(platformId, trainId, arrivalTime, departureTime);
    }
}
