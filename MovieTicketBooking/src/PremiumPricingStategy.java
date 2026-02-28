class PremiumPricingStategy implements PricingStrategy {

    private final static double fare = 20.0;

    @Override
    public double getFare() {
        return fare;
    }
}
