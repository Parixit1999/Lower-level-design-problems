public class CounterThread implements Runnable{

    private final State state;
    private PrintType type;
    private int maximum;

    public CounterThread(State state, PrintType type, int limit) {
        this.state = state;
        this.type = type;
        this.maximum = limit;
    }

    public void run() {
        synchronized (state) {

            while (true) {
                if (this.type == PrintType.EVEN) {
                    while(state.getValue() % 2 != 0) {
                        try {
                            state.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                } else{

                    while(state.getValue() % 2 != 1) {
                        try {
                            state.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                if(state.getValue() > maximum) {
                    state.notifyAll();
                    break;
                }

                System.out.println(state.getValue());
                state.increment();
                state.notifyAll();
            }
        }
    }
}
