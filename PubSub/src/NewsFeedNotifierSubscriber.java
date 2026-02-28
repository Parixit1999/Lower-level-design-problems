import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.*;;

class NewsFeedNotifierSubscriber extends AsyncSubscriber{

    public NewsFeedNotifierSubscriber() {
        super(1);
    }

    @Override
    protected void process(Message message) {
        System.out.println("Alter notification: " + message.payload);
    }
}
