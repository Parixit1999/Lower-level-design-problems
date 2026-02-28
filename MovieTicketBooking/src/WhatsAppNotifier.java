import java.util.*;

class WhatsAppNotifier implements Notifier{
    @Override
    public void sendMessage(Ticket ticket) {
        System.out.println("Sending ticket: " + ticket.getId() + " to WhatsApp!");
    }
}
