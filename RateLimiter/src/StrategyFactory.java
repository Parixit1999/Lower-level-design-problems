import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

public class StrategyFactory {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public static RateLimiterStrategy getStrategy(RateLimiterStrategyType type, int rate) {

        return switch (type) {
            case RateLimiterStrategyType.TOKEN_BUCKET -> {

                TokenBucketStrategy strategy = new TokenBucketStrategy(rate);
                scheduler.scheduleAtFixedRate(strategy::refill, 1000, 1000L / strategy.getRate(), TimeUnit.MICROSECONDS);
                yield strategy;
            }

            case RateLimiterStrategyType.TIME_WINDOW -> new TimeWindowStrategy(rate);
        };
    }
}
