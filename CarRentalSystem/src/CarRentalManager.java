class CarRentalManager implements CarRentalManagerOps {
    ConcurrentHashMap<String,Car> licenseToCarMap;
    ConcurrentHashMap<String, List<Order>> carBooking;
    ConcurrentHashMap<String, Order> orderMap;
    RentalPricingStrategy rentalPricingStrategy;


    public  CarRentalManager(RentalPricingStrategy rentalPricingStrategy) {
        licenseToCarMap = new ConcurrentHashMap<>();
        carBooking = new ConcurrentHashMap<>();
        orderMap = new ConcurrentHashMap<>();
        this.rentalPricingStrategy = rentalPricingStrategy;
    }

    @Override
    public void addCar(String licensePlate, int costPerDay, int freeKmsPerDay, int costPerKm) {
        if(!licenseToCarMap.containsKey(licensePlate)) {
            // TODO: Add factory following DIP.
                licenseToCarMap.put(licensePlate, new Car(licensePlate, costPerDay, freeKmsPerDay, costPerKm));
        }
    }

    @Override
    public boolean bookCar(String orderId, String carLicensePlate, String fromDate, String tillDate) {
        if(licenseToCarMap.containsKey(carLicensePlate)) {
            // TODO: Add facotry to generate orders. Following DIP and SRP.
            synchronized (licenseToCarMap.get(carLicensePlate)) {
                if(carBooking.containsKey(carLicensePlate)){
                    for(Order order: carBooking.get(carLicensePlate)) {
                        if(DateExtractor.extractDay(order.fromDate) <= DateExtractor.extractDay(tillDate) && DateExtractor.extractDay(fromDate) <= DateExtractor.extractDay(order.toDate)){
                            System.out.println("Car:" + carLicensePlate + " already booked for order: " + orderId);
                            return false;
                        }
                    }
                }

                Order newOrder = new Order(orderId, licenseToCarMap.get(carLicensePlate), fromDate, tillDate);
                carBooking.compute(carLicensePlate, (key, value) -> {
                    if(value == null) {
                        value = new ArrayList<>();
                    }
                    value.add(newOrder);
                    return value;
                });

                orderMap.put(orderId, newOrder);
                System.out.println("Car booked for order: " + orderId);
                return true;

            }
        }

        System.out.println("Car not found!");
        return false;
    }

    @Override
    public void startTrip(String orderId, int odometerReading) {
        Order order = this.orderMap.get(orderId);
        synchronized (order) {
            order.setStartingOdoMeterReading(odometerReading);
        }
    }

    @Override
    public int endTrip(String orderId, int finalOdometerReading, String endDate) {
        Order order = this.orderMap.get(orderId);
        synchronized (order) {
            double cost = rentalPricingStrategy.getPrice(order, finalOdometerReading, endDate);
            System.out.println("Ending the trip!...Cost: " + cost + " dollars.");
            return (int) cost;
        }
    }
}
