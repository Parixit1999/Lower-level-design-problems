public class State {
    private int value;

    public State(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void increment() {
        this.value++;
    }
}
