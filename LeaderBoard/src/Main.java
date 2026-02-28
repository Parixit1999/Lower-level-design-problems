import java.util.*;
import java.util.concurrent.*;

public class Main {
public static void main(String[] args) throws InterruptedException {
    LeaderBoard lb = new LeaderBoard("World Cup");

    // 1. Initialize Players
    Player pA = new Player("Player_A");
    Player pB = new Player("Player_B");
    Player pC = new Player("Player_C");

    // 2. Initialize Users with overlapping Players
    User u1 = new User("User_1", Arrays.asList(pA, pB));    // Total: A + B
    User u2 = new User("User_2", Arrays.asList(pA, pC));    // Total: A + C
    User u3 = new User("User_3", Arrays.asList(pB));        // Total: B

    lb.addUser(u1);
    lb.addUser(u2);
    lb.addUser(u3);

    int totalThreads = 60; // Divisible by 3 for even distribution
    ExecutorService executor = Executors.newFixedThreadPool(15);
    CountDownLatch latch = new CountDownLatch(1);
    CountDownLatch finishLine = new CountDownLatch(totalThreads);

    // 3. Submit 60 threads (20 threads per player, each adding 5 points)
    for (int i = 0; i < totalThreads; i++) {
        final int threadId = i;
        executor.submit(() -> {
            try {
                latch.await(); // All threads start at the same time
                if (threadId % 3 == 0) lb.addScore(pA, 5);
                else if (threadId % 3 == 1) lb.addScore(pB, 5);
                else lb.addScore(pC, 5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                finishLine.countDown();
            }
        });
    }

    latch.countDown(); // BANG! The race starts.
    finishLine.await();
    executor.shutdown();

    // 4. Expected Math
    // Each player was updated 20 times with 5 points = 100 points each.
    // User 1: pA(100) + pB(100) = 200
    // User 2: pA(100) + pC(100) = 200
    // User 3: pB(100)           = 100

    System.out.println("Final Player Scores: A=" + pA.getScore() + ", B=" + pB.getScore() + ", C=" + pC.getScore());

    List<String> top3 = lb.getTopK(3);
    System.out.println("Final Leaderboard: " + top3);

    // Verification Logic:
    // User 1 and 2 are tied at 200. Tie-breaker is ID "User_1" vs "User_2".
    // "User_1" comes first alphabetically.
}
}
