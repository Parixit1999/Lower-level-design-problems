public class Main {
public static void main(String[] args) {

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
}
