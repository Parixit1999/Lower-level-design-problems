
import java.util.*;
import java.util.concurrent.*;

class Message{
    String id;

    public Message() {
        this.id = UUID.randomUUID().toString();;
    }
}
