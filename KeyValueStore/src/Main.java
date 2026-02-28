public class Main {
public static void main(String[] args) throws InterruptedException {
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
}
