import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.*;;

class Message{
    String payload;

    public Message(String payload) {
        this.payload = payload;
    }
}
