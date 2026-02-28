class Request {
    private String url;

    public Request(String url) {
        this.url = url;
    }
}

interface Content<T> {
    public T getContent();
}

class WebPage<T> implements Content<T> {
    private String url;
    private T content;

    public WebPage(String url, T content) {
        this.url = url;
        this.content = content;
    }

    public String getUrl() {
        return this.url;
    }

    public T getContent() {
        return content;
    }
}

interface Cache<K, V> {
    void put(K key, V value);
    Optional<V> get(K key);
}

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

void main() throws InterruptedException {


    Cache<String, WebPage> lruCache = new LRUCache(5);

    WebPage webPage1 = new WebPage("/api/order", "order");
    WebPage webPage2 = new WebPage("/api/admin", "admin");
    WebPage webPage3 = new WebPage("/api/login", "login");
    WebPage webPage4 = new WebPage("/api/inventory", "inventory");
    WebPage webPage5 = new WebPage("/api/read", "read");
    WebPage webPage6 = new WebPage("/api/write", "write");

    lruCache.put(webPage1.getUrl(), webPage1);
    lruCache.put(webPage2.getUrl(), webPage2);
    lruCache.put(webPage3.getUrl(), webPage3);
    lruCache.put(webPage4.getUrl(), webPage4);
    lruCache.put(webPage5.getUrl(), webPage5);
    lruCache.put(webPage6.getUrl(), webPage6);

    System.out.println(lruCache.get("/api/order"));

    System.out.println(lruCache.get("/api/write").get().getContent());
    System.out.println(lruCache.get("/api/admin").get().getContent());

    ExecutorService executors = Executors.newFixedThreadPool(5);

    System.out.println("================");
    Thread.sleep(1000);
    executors.submit(()-> lruCache.put(webPage1.getUrl(), webPage1));
    executors.submit(()-> lruCache.put(webPage2.getUrl(), webPage2));
    executors.submit(()-> lruCache.put(webPage3.getUrl(), webPage3));
    executors.submit(()-> lruCache.put(webPage4.getUrl(), webPage4));
    executors.submit(()-> lruCache.put(webPage5.getUrl(), webPage5));
    Thread.sleep(1000);
    System.out.println("================");
}
