interface RentalPricingStrategy{

    // TODO: Just passed endTripMetadata.
    double getPrice(Order order, int finalOdometerReading, String endDate);

}
