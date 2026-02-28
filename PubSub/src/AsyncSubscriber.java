import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.*;;

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
