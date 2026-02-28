class LRUCache<K, V> implements Cache<K, V> {
    private LinkedHashMap<K, V> lruCache = new LinkedHashMap<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void put(K key, V value) {
        lock.writeLock().lock();
        try{
            if(lruCache.size() == capacity) {
                if(lruCache.containsKey(key)) {
                    lruCache.remove(key);
                } else{
                    lruCache.pollLastEntry();
                }
                lruCache.putFirst(key, value);
            } else{
                System.out.println("Cache size: " +  lruCache.size());
                lruCache.putFirst(key, value);
            }
        } finally {
            lock.writeLock().unlock();
        }

    }

    @Override
    public Optional<V> get(K key) {
        lock.writeLock().lock();
        try{
            if(!lruCache.containsKey(key)) return Optional.empty();
            V content = lruCache.get(key);
            lruCache.remove(key);
            lruCache.putFirst(key, content);
            return Optional.of(content);

        } finally {
            lock.writeLock().unlock();
        }
    }
}
