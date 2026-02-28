interface KeyValueStore<K, V> {
    public boolean add(K key, V value);
    public V get(K key);
    public boolean remove(K key);
}
