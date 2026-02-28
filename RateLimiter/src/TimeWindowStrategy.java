import java.util.concurrent.*;

class TimeWindowStrategy implements RateLimiterStrategy {

    private ConcurrentLinkedDeque<Long> queue;
    // Rate per second
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
