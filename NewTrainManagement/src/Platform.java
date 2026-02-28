import java.awt.print.Book;

class Platform implements Comparable<Platform>{
    String id;

    public Platform(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(Platform o) {
        return this.id.compareTo(o.id);
    }
}
