import java.util.concurrent.atomic.*;

class Player {
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
