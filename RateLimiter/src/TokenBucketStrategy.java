import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

class TokenBucketStrategy implements RateLimiterStrategy {

    private AtomicInteger bucketSize = new AtomicInteger();
    private final int bucketLimit;
    @Getter
    private final int rate;

    public TokenBucketStrategy(int rate) {
        this.bucketLimit = rate;
        this.rate = rate;
        this.bucketSize.set(rate);
    }

    public void refill() {
        bucketSize.updateAndGet(current -> Math.min(current + 1, bucketLimit));
    }

    @Override
    public boolean isAllow() {
        return this.bucketSize.getAndUpdate(current -> current > 0 ? current - 1: 0) > 0;
    }
}
