import java.util.*;

class Room {

    private String id;
    private List<Seat> seats;

    public Room() {
        id = UUID.randomUUID().toString();
        seats = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void addSeat(Seat seat) {
        this.seats.add(seat);
    }

}
