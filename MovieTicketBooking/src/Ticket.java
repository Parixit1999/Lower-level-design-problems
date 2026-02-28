import java.util.*;

class Ticket {

    private String id;
    private long bookedAt;
    private boolean isBooked = false;
    private Seat seat;
    private Room room;

    public Ticket(Room room, Seat seat) {
        this.id = UUID.randomUUID().toString();
        this.room = room;
        this.seat = seat;
    }

    public void bookTicket(PricingStrategy pricingStrategy) {
        double fare = pricingStrategy.getFare();
        this.bookedAt = System.currentTimeMillis();
        isBooked = true;
        System.out.println("Total value for this ticket: " + fare);
    }

    public String getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public long getBookedAt() {
        return bookedAt;
    }

    public Seat getSeat() {
        return seat;
    }

    public boolean getIsBooked() {
        return isBooked;
    }
}
