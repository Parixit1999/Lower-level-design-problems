import java.util.*;

class MovieTicketBookingSystem {

    private static volatile MovieTicketBookingSystem movieTicketBookingSystemInstance;
    private List<Cinema> cinemas = new ArrayList<>();
    private  PricingStrategyFactory pricingStrategyFactory = new PricingStrategyFactory();
    private List<Notifier> notifiers = new ArrayList<>();

    Movie bhulboolaiya = new Movie("Bhulboolaiya");
    Movie ramleela = new Movie("Ramleela");
    Movie durandhar = new Movie("Durandhar");

    private MovieTicketBookingSystem() {
        cinemas.add(new Cinema(CinemaCompany.INOX, 5));
        cinemas.add(new Cinema(CinemaCompany.AMX, 5));
        cinemas.add(new Cinema(CinemaCompany.ALPHA, 5));

        for(Cinema cinema: cinemas) {
            cinema.addShow(bhulboolaiya, 1, System.currentTimeMillis(), System.currentTimeMillis() + 360000);
            cinema.addShow(ramleela, 1, System.currentTimeMillis(), System.currentTimeMillis() + 360000);
            cinema.addShow(durandhar, 1, System.currentTimeMillis(), System.currentTimeMillis() + 360000);
        }

        notifiers.add(new EmailNotifier());
        notifiers.add(new WhatsAppNotifier());
    }

    private void sendMessage(Ticket ticket) {
        for(Notifier notifier: notifiers) {
            notifier.sendMessage(ticket);
        }
    }

    public static MovieTicketBookingSystem getInstance() {
        if(movieTicketBookingSystemInstance == null) movieTicketBookingSystemInstance = new MovieTicketBookingSystem();
        return movieTicketBookingSystemInstance;
    }

    public Cinema getCinemas(CinemaCompany cinemaCompany) {
        return (Cinema) cinemas.stream().filter(company -> company.getName() == cinemaCompany).toArray()[0];
    }

    public HashSet<Screen> showScreen(Cinema cinema) {
        return cinema.getShow();
    }

    public List<Ticket> showTicket(Screen screen) {
        System.out.println("Available ticket for screen: " + screen.getId() + " movie" + screen.getMovie());
        for(Ticket ticket: screen.getTicketList()) {
            System.out.println("Ticket ID: " + ticket.getId() + " Seat: " + ticket.getSeat());
        }
        return screen.getTicketList();
    }

    public List<Ticket> getTickets(Screen screen) {
        return screen.getTicketList();
    }

    public synchronized boolean bookTicket(Ticket ticket) {
       if (!ticket.getIsBooked()){
           ticket.bookTicket(pricingStrategyFactory.createPricingStrategy(ticket.getSeat().getType()));
           System.out.println("Ticket:" + ticket.getId() + " booked!");
           this.sendMessage(ticket);
           return true;
       } else{
            System.out.println("Ticket already booked!");
           return false;
       }

    }

}
