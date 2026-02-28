public class Main {
public static void main(String[] args) throws InterruptedException {


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
}
