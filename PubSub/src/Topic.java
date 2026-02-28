import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.*;;

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
