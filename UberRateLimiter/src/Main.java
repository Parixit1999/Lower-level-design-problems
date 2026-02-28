import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

public enum RateLimiterStrategyType{
    TOKEN_BUCKET,
    TIME_WINDOW
}

interface Identity {
    String getUniqueId();
}

@Getter
class User implements Identity {
    private String name;
    private String  id;

    public User(String name) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getUniqueId() {
        return this.id;
    }
}

interface RateLimiterStrategy {
    boolean isAllow();
}

static class TokenBucketStrategy implements RateLimiterStrategy {

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

static class TimeWindowStrategy implements RateLimiterStrategy {

    private ConcurrentLinkedDeque<Long> queue;
    // Rate per second
    @Getter
    private final int rate;

    public TimeWindowStrategy(int rate) {
        this.rate = rate;
        this.queue = new ConcurrentLinkedDeque();
    }

    @Override
    public boolean isAllow() {
        long now = System.currentTimeMillis();
        boolean ans = false;

        synchronized (this.queue) {
            if(this.queue.size() < this.rate) {
                this.queue.add(now);
                ans = true;
            } else{
                if(now - this.queue.getFirst() > 1000) {
                    this.queue.removeFirst();
                    ans = true;
                }

                this.queue.add(now);
            }
        }

        return ans;
    }
}

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

class Server {
    private RateLimiter rateLimiter;

    public Server(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public boolean request(Identity u) {
        return this.rateLimiter.isAllow(u);
    }
}

void main() throws InterruptedException {

    int rate = 10;
    RateLimiter rateLimiter = new RateLimiter(RateLimiterStrategyType.TIME_WINDOW, rate);
    Server server = new Server(rateLimiter);

    // 2. Define our users
    User userA = new User("User_A");
    User userB = new User("User_B");

    // 3. Simulate high-frequency traffic
    // We will fire 15 requests for User A and 15 for User B simultaneously
    int requestsPerUser = 100;
    ExecutorService executor = Executors.newFixedThreadPool(10);
    CountDownLatch latch = new CountDownLatch(1); // To start all threads at once

    AtomicInteger successA = new AtomicInteger();
    AtomicInteger successB = new AtomicInteger();

    Runnable taskA = () -> {
        try { latch.await(); } catch (InterruptedException e) {}
        if (server.request(userA)) successA.incrementAndGet();
    };

    Runnable taskB = () -> {
        try { latch.await(); } catch (InterruptedException e) {}
        if (server.request(userB)) successB.incrementAndGet();
    };

    // Submit tasks
    for (int i = 0; i < requestsPerUser; i++) {
        executor.submit(taskA);
        executor.submit(taskB);
    }

    System.out.println("Starting synchronized burst...");
    latch.countDown(); // Fire!

    executor.shutdown();
    executor.awaitTermination(5, TimeUnit.SECONDS);

    // 4. Results
    System.out.println("--- Results ---");
    System.out.println("User A Successes: " + successA.get() + " (Expected 10)");
    System.out.println("User B Successes: " + successB.get() + " (Expected 10)");

    // 5. Verify Replenishment
    System.out.println("\nWaiting 1.5 seconds for replenishment...");
    Thread.sleep(1500);

    boolean canRequestAgain = server.request(userA);
    System.out.println("User A can request again after sleep? " + canRequestAgain + " (Expected true)");

    System.out.println("\nWaiting 1.5 seconds for FULL replenishment...");
    Thread.sleep(1500);

    AtomicInteger burstSuccess = new AtomicInteger();
    for (int i = 0; i < 10; i++) {
        if (server.request(userA)) burstSuccess.incrementAndGet();
    }

    System.out.println("User A burst count: " + burstSuccess.get() + " (Expected 10");

}
