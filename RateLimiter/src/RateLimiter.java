import java.util.*;
import java.util.concurrent.*;

class RateLimiter {

    private ConcurrentHashMap<String, RateLimiterStrategy> bucketByUserId;
    private RateLimiterStrategyType rateLimiterStrategyType;
    private int rate;

    public RateLimiter(RateLimiterStrategyType rateLimiterStrategyType, int rate) {
        bucketByUserId = new ConcurrentHashMap<>();
        this.rateLimiterStrategyType = rateLimiterStrategyType;
        this.rate = rate;
    }

    public boolean isAllow(Identity id) {
//        System.out.println("Requested by: " + ((User) id).getName() + " at " + new Date(System.currentTimeMillis()));
        return bucketByUserId.computeIfAbsent(id.getUniqueId(),
                k -> StrategyFactory.getStrategy(this.rateLimiterStrategyType, this.rate)
        ).isAllow();
    }
}
