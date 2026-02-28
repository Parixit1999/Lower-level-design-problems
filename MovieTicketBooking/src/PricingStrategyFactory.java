class PricingStrategyFactory {
    public PricingStrategy createPricingStrategy(SeatType seatType){
        System.out.println(seatType);
        System.out.println(seatType == SeatType.NORMAL);
        switch (seatType) {
            case SeatType.NORMAL -> {
                return new NormalPricingStrategy();
            }
            case SeatType.PREMIUM -> {
                return new PremiumPricingStategy();
            }
            case SeatType.VIP -> {
                return new VIPPricingStrategy();
            }
        }

        throw new RuntimeException("Invalid seat type!");
    }
}
