import java.util.*;

class EmailNotifier implements Notifier{
    @Override
    public void sendMessage(Ticket ticket) {
        System.out.println("Sending ticket: " + ticket.getId() + " to Email!");
    }
}
