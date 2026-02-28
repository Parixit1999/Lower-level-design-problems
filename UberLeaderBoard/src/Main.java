import lombok.Getter;
import lombok.Setter;


class Player {
    @Getter
    private String id;

    private AtomicLong score = new AtomicLong(0);

    public Player(String id) {
        this.id = id;
        this.score.set(0);
    }

    public void setScore(long score) {
        this.score.getAndAdd(score);
    }

    public long getScore() {
        return this.score.get();
    }
}

@Getter
class User implements Comparable<User>{

    private String id;
    private List<Player> players;

    public User(String id, List<Player> players) {
        this.id = id;
        this.players = players;
    }

    public long getLatestPoint() {
        long points = 0;
        for(Player p: players) {
            points += p.getScore();
        }
        return points;
    }

    @Override
    public int compareTo(User o) {
        int result =  Long.compare(o.getLatestPoint(), this.getLatestPoint());

        if(result == 0) {
            return this.id.compareTo(o.getId());
        }

        return result;
    }
}


class LeaderBoard {

    private List<User> topUser;
    @Getter
    private String gameName;
    private ReentrantReadWriteLock lock;

    public LeaderBoard(String gameName) {
        this.gameName = gameName;
        this.topUser = new ArrayList<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public synchronized void addUser(User user) {
        this.topUser.add(user);
    }

    public void addScore(Player player, long score) {
        player.setScore(score);
    }

    public List<String> getTopK(int k) {

        this.lock.readLock().lock();

        try{
            Collections.sort(topUser);
            List<String> topKId = new LinkedList<>();

            for(int i = 0; i < Math.min(k, this.topUser.size()); i++) {
                User topUser = this.topUser.get(i);
                topKId.add(topUser.getId());
            }

            return topKId;
        } finally {
            this.lock.readLock().unlock();
        }

    }

}

void main() throws InterruptedException {
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
