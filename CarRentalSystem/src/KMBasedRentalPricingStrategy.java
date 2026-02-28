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
