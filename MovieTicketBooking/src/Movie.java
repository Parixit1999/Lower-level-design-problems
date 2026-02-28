import java.util.*;

class Movie {
    private String id;
    private String movieName;

    public Movie(String movieName) {
        this.id = UUID.randomUUID().toString();
        this.movieName = movieName;
    }

    public String getId() {
        return id;
    }

    public String getMovieName() {
        return movieName;
    }
}
