import lombok.Getter;

public enum Priority {
  LOW(1),
  MEDIUM(2),
  HIGH(3);

  private final int priority;
  Priority(int priority) {
    this.priority = priority;
  }

  public int getPriority() {
    return this.priority;
  }

}

@Getter
public class Job implements Comparable<Job> {
  private String name;
  private Priority priority;
  private int processTime;

  public Job(String name, Priority priority, int processTime) {
    this.name = name;
    this.priority = priority;
    this.processTime = processTime;
  }

  @Override
  public int compareTo(Job o) {
    int result = o.priority.getPriority() - this.priority.getPriority();
    if (result == 0) return o.processTime - this.processTime;

    return result;
  }
}


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



void main() {
  UberTaskScheduler scheduler = new UberTaskScheduler(2);

  System.out.println("--- Starting Scheduler ---");
  scheduler.start();

  scheduler.submit(new Job("Background-Cleanup", Priority.LOW, 2000));

  scheduler.submit(new Job("Critical-Payment-Fix", Priority.HIGH, 1000));
  scheduler.submit(new Job("User-Update-Medium", Priority.MEDIUM, 500));
  scheduler.submit(new Job("High-Priority-Alert", Priority.HIGH, 1000));
  scheduler.submit(new Job("Email-Notification", Priority.LOW, 500));

  try {
    Thread.sleep(200); // Wait slightly so workers grab the first two jobs
    System.out.println("--- Submitting Emergency-Patch while others run ---");
    scheduler.submit(new Job("Emergency-Patch", Priority.HIGH, 300));
  } catch (InterruptedException e) {
    Thread.currentThread().interrupt();
  }

  try {
    Thread.sleep(6000);
  } catch (InterruptedException e) {
    Thread.currentThread().interrupt();
  }

  System.out.println("--- Shutting Down ---");
  scheduler.shutdown();

}
