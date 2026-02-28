
interface CarRentalManagerOps{
    void addCar(String licensePlate, int costPerDay, int freeKmsPerDay, int costPerKm);
    boolean bookCar(String orderId, String carLicensePlate, String fromDate, String tillDate);
    void startTrip(String orderId, int odometerReading);
    int endTrip(String orderId, int finalOdometerReading, String endDate);
}

class DateExtractor{

    public static int extractDay(String date) {
        String [] newDate = date.split("-");
        return Integer.parseInt(newDate[newDate.length - 1]);
    }
}

interface RentalPricingStrategy{

    // TODO: Just passed endTripMetadata.
    double getPrice(Order order, int finalOdometerReading, String endDate);

}

class KMBasedRentalPricingStrategy implements RentalPricingStrategy{

    @Override
    public double getPrice(Order order, int finalOdometerReading, String endDate) {
        int effectiveDate = Math.max(DateExtractor.extractDay(order.toDate), DateExtractor.extractDay(endDate));
        int days = 1 + (effectiveDate - DateExtractor.extractDay(order.fromDate));
        int chargeableKm = Math.max(finalOdometerReading - order.startingOdoMeterReading - (order.car.freeKmsPerDay) * days, 0);
//        System.out.println(chargeableKm + " " + order.car.costPerKm + " " + order.car.costPerDay + " " +days);
        return chargeableKm * order.car.costPerKm + order.car.costPerDay * days;
    }
}

class Car {

    String carLicensePlate;
    int costPerDay;
    int freeKmsPerDay;
    int costPerKm;

    public Car(String carLicensePlate, int costPerDay, int freeKmsPerDay, int costPerKm) {
        this.carLicensePlate = carLicensePlate;
        this.costPerDay = costPerDay;
        this.freeKmsPerDay = freeKmsPerDay;
        this.costPerKm = costPerKm;
    }


    // TODO: Getters.

}

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


void main() {

    RentalPricingStrategy rentalPricingStrategy = new KMBasedRentalPricingStrategy();

    CarRentalManagerOps carRentalManagerOps = new CarRentalManager(rentalPricingStrategy);

    carRentalManagerOps.addCar("KA01AB1234", 1200, 100, 10);
    carRentalManagerOps.bookCar("ORD-1", "KA01AB1234", "2025-08-28", "2025-08-30");
    carRentalManagerOps.startTrip("ORD-1", 5000);
    carRentalManagerOps.endTrip("ORD-1", 5250, "2025-08-29");

    carRentalManagerOps.addCar("MH12EF9999", 1000, 80, 12);
    carRentalManagerOps.bookCar("ORD-3", "MH12EF9999", "2025-08-10", "2025-08-12");
    carRentalManagerOps.bookCar("ORD-4", "MH12EF9999", "2025-08-12", "2025-08-15");
}
