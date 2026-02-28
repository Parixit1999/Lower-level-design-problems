class HourlyPricingStrategy implements  PricingStrategy {
    @Override
    public double calculateAmount(long hours) {
        if(hours < 1) return 10.0;
        return hours * 20.0;
    }
}
