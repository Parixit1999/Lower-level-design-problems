
import java.util.*;
import java.util.concurrent.*;

class TicketFactory {
    public Ticket createTicket(Vehicle vehicle, ParkingSpot parkingSpot) {
        return new Ticket(vehicle, parkingSpot);
    }
}
