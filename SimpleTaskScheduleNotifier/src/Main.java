
import java.util.*;
import java.util.concurrent.*;

class Task implements Comparable<Task>{
    Message message;
    Long time;

    public Task(Message message, Long time) {
        this.message = message;
        this.time = time;
    }

    @Override
    public int compareTo(Task o) {
        return Long.compare(this.time, o.time);
    }
}

class Message {
    String payload;

    Message(String payload) {
        this.payload = payload;
    }
}

interface Notifier{
    void onNotify(Message message);
}


class RideNotifier implements Notifier{
    public void onNotify(Message message) {
        System.out.println(message.payload + " " + new Date(System.currentTimeMillis()));
    }
}

interface Scheduler {
    void scheduleTask(Task task);
}

class RideScheduler implements Scheduler{
    private Notifier notifier;
    private ExecutorService excuter = Executors.newFixedThreadPool(2);
    private PriorityBlockingQueue<Task> scheduledTask = new PriorityBlockingQueue<>();
    private Object lock = new Object();

    public RideScheduler(Notifier notifier) {
        this.notifier = notifier;
        new Thread(this::start).start();
    }

    public void start() {
        while(true){

            synchronized(lock){
                try{
                    while(scheduledTask.isEmpty()) {
                        lock.wait();
                    }
                    Task topElement = scheduledTask.peek();
                    Long timeDiff = topElement.time - System.currentTimeMillis();
                    if(timeDiff <= 0) {
                        Task taskToRun = scheduledTask.poll();
                        if(taskToRun != null){
                            excuter.submit(()-> this.notifier.onNotify(taskToRun.message));
                        }
                        lock.notify();

                    } else{
                        lock.wait(timeDiff);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void scheduleTask(Task task) {
        synchronized(lock){
            scheduledTask.add(task);
            lock.notify();
        }
    }

    public void end() {
        excuter.shutdown();
    }
}


void main(String[] args) throws InterruptedException {

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

