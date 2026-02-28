
interface HitCountable {
    void incrementHitCount(int pageIdx);
    int getVisitCount(int pageIdx);
}

class HitCounterForWeb implements HitCountable {

    private ConcurrentHashMap<Integer, Integer> hashMap;
    private int numberOfPages;
    private final int POOL_SIZE = 1024;

    public HitCounterForWeb(int numberOfPages) {
        this.hashMap = new ConcurrentHashMap<>();
        this.numberOfPages = numberOfPages;
    }

    @Override
    public void incrementHitCount(int pageIdx) {

        if(pageIdx < 0 || pageIdx >= this.numberOfPages) {
            throw new RuntimeException("Invalid page index");
        }

        hashMap.merge(pageIdx, 1, Integer::sum);
    }

    @Override
    public int getVisitCount(int pageIdx) {
        return hashMap.getOrDefault(pageIdx, 0);
    }
}

void main() throws InterruptedException {
    int highPageIdx = 200;
    int numberOfPages = 201;
    HitCounterForWeb counter = new HitCounterForWeb(numberOfPages);

    int incrementsPerThread = 10000;
    int numThreads = 4;
    Thread[] threads = new Thread[numThreads];

    System.out.println("Testing Page Index: " + highPageIdx);
    System.out.println("Expected total: " + (numThreads * incrementsPerThread));

    for (int i = 0; i < numThreads; i++) {
        threads[i] = new Thread(() -> {
            for (int j = 0; j < incrementsPerThread; j++) {
                counter.incrementHitCount(highPageIdx);
            }
        });
        threads[i].start();
    }

    for (Thread t : threads) t.join();

    int actual = counter.getVisitCount(highPageIdx);
    int expected = numThreads * incrementsPerThread;

    System.out.println("Actual Count:   " + actual);

    if (actual < expected) {
        System.out.println("\n[!] FAILURE DETECTED: Lost " + (expected - actual) + " hits!");
        System.out.println("Reason: synchronized((Integer)200) created different locks for different threads.");
    } else {
        System.out.println("\nSUCCESS: No hits lost (unlikely with high page index).");
    }

}
