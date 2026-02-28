class Order{

    String orderId;
    String fromDate;
    String toDate;
    Car car;
    int startingOdoMeterReading = 0;

    public Order(String orderId, Car car, String fromDate, String toDate) {
        this.orderId = orderId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.car = car;
    }

    public synchronized void setStartingOdoMeterReading(int reading) {
        this.startingOdoMeterReading = reading;
    }

}
