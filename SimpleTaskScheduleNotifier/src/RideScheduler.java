import java.util.concurrent.*;

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
