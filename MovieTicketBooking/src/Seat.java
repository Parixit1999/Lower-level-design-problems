import java.util.*;

abstract class Seat {
    private final String id;
    private final SeatType type;

    public Seat(SeatType type) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public SeatType getType() {
        return type;
    }
}
