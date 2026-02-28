interface CarRentalManagerOps{
    void addCar(String licensePlate, int costPerDay, int freeKmsPerDay, int costPerKm);
    boolean bookCar(String orderId, String carLicensePlate, String fromDate, String tillDate);
    void startTrip(String orderId, int odometerReading);
    int endTrip(String orderId, int finalOdometerReading, String endDate);
}
