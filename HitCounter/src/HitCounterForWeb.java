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
