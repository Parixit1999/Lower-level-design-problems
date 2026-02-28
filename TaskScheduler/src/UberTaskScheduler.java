import lombok.Getter;
import lombok.SneakyThrows;

public class UberTaskScheduler extends TaskScheduler {

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    public UberTaskScheduler(int numberOfThreads) {
        super(numberOfThreads);
    }

    @SneakyThrows
    @Override
    public void start() {

        if(isRunning.get()) return;

        isRunning.set(true);

        int numberOfThreads = ((ThreadPoolExecutor) this.threadPool).getCorePoolSize();

        for(int i = 0; i < numberOfThreads; i++) {
            this.threadPool.submit(this::workLoop);
        }
    }

    private void workLoop() {

        try {
            while(isRunning.get()) {
                Task taskToProcess = null;
                long sleepTime = 0;

                synchronized (this.priorityBlockingQueue) {
                    Task top = this.priorityBlockingQueue.peek();
                    if (top == null) {
                        sleepTime = 200;
                    } else {
                        long now = System.currentTimeMillis();
                        if (top.getStartTime() <= now) {
                            taskToProcess = this.priorityBlockingQueue.poll();
                        } else {
                            sleepTime = top.getStartTime() - now;
                        }
                    }
                }

                if (taskToProcess != null) {
                    process(taskToProcess);
                } else {
                    Thread.sleep(Math.min(sleepTime, 1000)); // Sleep WITHOUT holding the lock
                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private void process(Task task) {
        System.out.println( task.getName() + " is being executing at " + new Date(System.currentTimeMillis()) + "!");
        long nextRun = task.getStartTime() + task.getInterval();
        synchronized (this.priorityBlockingQueue) {
            this.priorityBlockingQueue.add(new Task(task.getName(), nextRun, task.getInterval()));
        }
    }

    @Override
    public void submitTask(Task t) {
        synchronized (this.priorityBlockingQueue) {
            this.priorityBlockingQueue.add(t);
        }
    }

    @Override
    public void stop() {
        System.out.println("Shutting down scheduler!");
        this.isRunning.set(false);
        this.threadPool.shutdown();

    }
}
