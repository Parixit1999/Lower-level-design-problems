import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.*;;



class Message{
    String payload;

    public Message(String payload) {
        this.payload = payload;
    }
}

interface Subscriber {
    void onUpdate(Message message);
}

abstract class AsyncSubscriber implements Subscriber {
    protected final ExecutorService executorService;

    protected AsyncSubscriber(int poolSize) {
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    // Common logic moved here
    public void stop() {
        executorService.shutdown();
    }

    // Forces children to implement the logic, but handles the 'submit' here
    @Override
    public void onUpdate(Message message) {
        executorService.submit(() -> process(message));
    }

    protected abstract void process(Message message);
}

class AlertNotifierSubscriber extends AsyncSubscriber{

    public AlertNotifierSubscriber() {
        super(1);
    }

    @Override
    protected void process(Message message) {
        System.out.println("Alter notification: " + message.payload);
    }

}

class NewsFeedNotifierSubscriber extends AsyncSubscriber{

    public NewsFeedNotifierSubscriber() {
        super(1);
    }

    @Override
    protected void process(Message message) {
        System.out.println("Alter notification: " + message.payload);
    }
}

class Topic {

    String id;
    CopyOnWriteArraySet<Subscriber> subscribers;

    public Topic(String id) {
        this.id = id;
        this.subscribers = new CopyOnWriteArraySet<>();
    }

    public void subScribe(Subscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    public void unSubScribe(Subscriber subscriber) {
        this.subscribers.remove(subscriber);
    }

    public void addMessage(Message message){
        for(Subscriber subscriber: this.subscribers) {
            subscriber.onUpdate(message);
        }
    }
}



static class PubSubQueueService{

    static PubSubQueueService instance;
    ConcurrentHashMap<String, Topic> topicHashMap = new ConcurrentHashMap<>();

    private PubSubQueueService() {

    }

    public static PubSubQueueService getInstance() {
        if(instance == null) {
            synchronized (PubSubQueueService.class){
                if(instance == null){
                    instance = new PubSubQueueService();
                }
            }
        }

        return instance;
    }

    public void createTopic(Topic topic) {
        this.topicHashMap.putIfAbsent(topic.id, topic);
    }

    public boolean subscribeTopic(String topicId, Subscriber subscriber) {

        if(topicHashMap.containsKey(topicId)){
            synchronized(topicHashMap.get(topicId)) {
                topicHashMap.get(topicId).subScribe(subscriber);
            }
            return true;
        }
        return false;
    }

    public void publishMessage(String topicId, Message message) {
        this.topicHashMap.get(topicId).addMessage(message);
    }
}


void main(String[] args) {

    PubSubQueueService pubSubQueueService = PubSubQueueService.getInstance();

    pubSubQueueService.createTopic(new Topic("1"));
    pubSubQueueService.createTopic(new Topic("2"));
    pubSubQueueService.createTopic(new Topic("3"));

    Subscriber alertSubscriber = new AlertNotifierSubscriber();
    Subscriber newsSubscriber = new NewsFeedNotifierSubscriber();

    System.out.print(pubSubQueueService.subscribeTopic("1", alertSubscriber));
    pubSubQueueService.subscribeTopic("1", newsSubscriber);

    pubSubQueueService.subscribeTopic("2", alertSubscriber);

    pubSubQueueService.publishMessage("1", new Message("World cup scheduled on 2026!"));
    pubSubQueueService.publishMessage("2", new Message("Modi visited US!"));

}
