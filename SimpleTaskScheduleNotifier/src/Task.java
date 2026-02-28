
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
