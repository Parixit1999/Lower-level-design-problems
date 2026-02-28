import java.util.concurrent.*;

class PubSubQueueService{

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
