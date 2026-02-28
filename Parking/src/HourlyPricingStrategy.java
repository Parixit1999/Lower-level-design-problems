import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

class HourlyPricingStrategy implements  PricingStrategy {
    @Override
    public double calculateAmount(long hours) {
        if(hours < 1) return 10.0;
        return hours * 20.0;
    }
}
