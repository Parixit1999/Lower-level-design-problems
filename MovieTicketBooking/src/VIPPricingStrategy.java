import java.util.*;

class VIPPricingStrategy implements PricingStrategy {

    private final static double fare = 30.0;

    @Override
    public double getFare() {
        return fare;
    }
}
