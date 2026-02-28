import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

public class Main {
public static void main(String[] args) throws InterruptedException {

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
}
