import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class UberTaskScheduler {
  private final PriorityBlockingQueue<Job> jobQueue = new PriorityBlockingQueue<>();
  private final ExecutorService executorPool;
  private final AtomicBoolean isRunning = new AtomicBoolean(false);

  public UberTaskScheduler(int threadCount) {
    // Using a pool is cleaner than managing a Thread[] array
    this.executorPool = Executors.newFixedThreadPool(threadCount);
  }

  public void start() {
    if (isRunning.getAndSet(true)) return; // Prevent double starting

    int threadCount = ((ThreadPoolExecutor) executorPool).getCorePoolSize();
    for (int i = 0; i < threadCount; i++) {
      executorPool.submit(this::workerLoop);
    }
  }

  private void workerLoop() {
    while (isRunning.get() || !jobQueue.isEmpty()) {
      try {
        // Use poll with timeout so threads can eventually exit if isRunning is false
        Job job = jobQueue.poll(500, TimeUnit.MILLISECONDS);
        if (job != null) {
          process(job);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }

  public void submit(Job job) {
    jobQueue.offer(job);
  }

  private void process(Job job) {
    System.out.printf("[%s] Executing: %s (Priority: %s, Time: %dms)%n",
            Thread.currentThread().getName(), job.getName(), job.getPriority(), job.getProcessTime());
    try {
      Thread.sleep(job.getProcessTime());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void shutdown() {
    isRunning.set(false);
    executorPool.shutdown();
  }
}
