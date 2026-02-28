import lombok.Getter;

public class Main {
public static void main(String[] args) {
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
}
