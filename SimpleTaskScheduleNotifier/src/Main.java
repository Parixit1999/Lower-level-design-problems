import java.util.*;

public class Main {
public static void main(String[] args) throws InterruptedException {

    Notifier notifier = new RideNotifier();

    RideScheduler rideScheduler = new RideScheduler(notifier);

    System.out.println(new Date(System.currentTimeMillis()));
    rideScheduler.scheduleTask(new Task(new Message("Hello"), System.currentTimeMillis() + 5000L));
    rideScheduler.scheduleTask(new Task(new Message("Parixit"), System.currentTimeMillis() + 2000L));
    rideScheduler.scheduleTask(new Task(new Message("Sanghani"), System.currentTimeMillis() + 4000L));
    rideScheduler.scheduleTask(new Task(new Message("Dineshbhai"), System.currentTimeMillis() + 1000L));

    Thread.sleep(1000);
    rideScheduler.scheduleTask(new Task(new Message("Hello Urgen"), System.currentTimeMillis() + 100L));

    try {

        Thread.sleep(10000);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
