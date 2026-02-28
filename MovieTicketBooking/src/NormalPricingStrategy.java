import java.util.*;

class NormalPricingStrategy implements PricingStrategy {

    private final static double fare = 10.0;

    @Override
    public double getFare() {
        return fare;
    }
}
