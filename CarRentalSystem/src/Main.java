public class Main {
public static void main(String[] args) {

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
}
