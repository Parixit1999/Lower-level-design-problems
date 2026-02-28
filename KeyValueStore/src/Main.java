
interface KeyValueStore<K, V> {
    public boolean add(K key, V value);
    public V get(K key);
    public boolean remove(K key);
}

public class TimeToLiveKeyValueStore<K, V> implements KeyValueStore<K, V> {

    LinkedHashMap<K, V> hashMap = new LinkedHashMap<>();
    ConcurrentHashMap<K, Long> timeTracker = new ConcurrentHashMap<>();
    int timeToLive;
    Object lock = new Object();

    public TimeToLiveKeyValueStore(int timeToLive) {
        this.timeToLive = timeToLive;
        new Thread(this::removeOldElement).start();
    }

    private void removeOldElement() {
        while(true) {
            synchronized (lock){

                if(!hashMap.isEmpty()) {
                    K firstValue = hashMap.firstEntry().getKey();
                    if(timeTracker.get(firstValue) + timeToLive < System.currentTimeMillis()) {
                        hashMap.remove(firstValue);
                    } else{
                        try{
                            lock.wait(timeTracker.get(firstValue) + timeToLive - System.currentTimeMillis());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public synchronized boolean add(K key, V value) {
        timeTracker.put(key, System.currentTimeMillis());
        hashMap.put(key, value);
        return true;
    }

    @Override
    public V get(K key) {
        synchronized (lock) {
            timeTracker.put(key, System.currentTimeMillis());
            V value = hashMap.remove(key);
            hashMap.put(key, value);
            lock.notify();
        }
        return hashMap.get(key);
    }

    @Override
    public boolean remove(K key) {
        synchronized (lock){
            if(hashMap.containsKey(key)) {
                hashMap.remove(key);
                lock.notify();
                return true;
            }
            return false;
        }
    }
}



void main() throws InterruptedException {
    KeyValueStore<Integer, Integer> keyValueStore = new TimeToLiveKeyValueStore<>(2000);

    keyValueStore.add(1, 10);
    keyValueStore.add(2, 10);
    keyValueStore.add(3, 10);

    System.out.println(keyValueStore.get(1));
    System.out.println(keyValueStore.get(2));
    System.out.println(keyValueStore.get(3));

    Thread.sleep(2000);

    System.out.println(keyValueStore.get(1));
    System.out.println(keyValueStore.get(2));
    System.out.println(keyValueStore.get(3));

}
