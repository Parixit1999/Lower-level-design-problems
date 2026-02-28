import java.util.*;

class RideNotifier implements Notifier{
    public void onNotify(Message message) {
        System.out.println(message.payload + " " + new Date(System.currentTimeMillis()));
    }
}
