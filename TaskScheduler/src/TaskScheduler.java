import java.util.*;
import java.util.concurrent.*;

public abstract class TaskScheduler {

    protected final PriorityQueue<Task> priorityBlockingQueue;
    protected ExecutorService threadPool;

    public TaskScheduler(int numberOfThreads) {
        threadPool = Executors.newFixedThreadPool(numberOfThreads);
        priorityBlockingQueue = new PriorityQueue<>();
    }

    public abstract void start();
    public abstract void submitTask(Task t);
    public abstract void stop();

}
