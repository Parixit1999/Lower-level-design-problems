import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

class NotificationService {
    private final List<TicketObserver> observers = new ArrayList<>();

    public void addObserver(TicketObserver observer) {
        observers.add(observer);
    }

    public void notifyObserversForTicketIssued(ParkingTicket ticket) {
        for (TicketObserver observer : observers) {
            observer.onTicketGenerated(ticket);
        }
    }

    public void notifyObserversForTicketCleared(ParkingTicket ticket) {
        for (TicketObserver observer : observers) {
            observer.onTicketCleared(ticket);
        }
    }
}
