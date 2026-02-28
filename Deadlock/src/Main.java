public class Nameste {

    private final String name;
    private Boolean status;

    public Nameste(String name) {
        this.name = name;
        this.status = false;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void bow(Nameste to) {
        synchronized (this) {
            System.out.println("Nameste from: " +  this.name);
            this.status = true;

            this.notifyAll();
            while(!to.getStatus()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            this.notifyAll();

        }

        to.bowUp();

    }

    public void bowUp() {
        synchronized (this) {
            System.out.println("Nameste done: " +  this.name);
        }
    }
}

void main() {
    Nameste parixit = new Nameste("Parixit");
    Nameste megha = new Nameste("Megha");

    Thread thread1 = new Thread(() -> parixit.bow(megha));
    Thread thread2 = new Thread(() -> megha.bow(parixit));

    thread1.start();
    thread2.start();

}
