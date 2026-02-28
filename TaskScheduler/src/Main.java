import lombok.Getter;
import lombok.SneakyThrows;

public class Main {
public static void main(String[] args) throws InterruptedException {

    UberTaskScheduler taskScheduler = new UberTaskScheduler(5);

    taskScheduler.start();

    taskScheduler.submitTask(new Task("Parixit", System.currentTimeMillis() + 5000, 2000));
    taskScheduler.submitTask(new Task("Sanghani", System.currentTimeMillis() + 3000, 5000));


    Thread.sleep(60000);
    taskScheduler.stop();

}
}
