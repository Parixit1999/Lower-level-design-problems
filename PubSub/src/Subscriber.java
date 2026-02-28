import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.*;;

interface Subscriber {
    void onUpdate(Message message);
}
