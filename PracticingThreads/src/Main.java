//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {
public static void main(String[] args) {

    int limit = 100;
    State state = new State(1);

    Thread oddThread = new Thread(new CounterThread(state, PrintType.ODD, limit));
    Thread evenThread = new Thread(new CounterThread(state, PrintType.EVEN, limit));

    oddThread.start();
    evenThread.start();
}
}
