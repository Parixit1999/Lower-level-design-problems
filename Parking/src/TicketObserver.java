import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

interface TicketObserver {
    void onTicketGenerated(ParkingTicket ticket);
    void onTicketCleared(ParkingTicket ticket);
}
