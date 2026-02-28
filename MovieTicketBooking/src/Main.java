//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {
public static void main(String[] args) {
    MovieTicketBookingSystem movieTicketBookingSystem = MovieTicketBookingSystem.getInstance();

    Cinema cinema = movieTicketBookingSystem.getCinemas(CinemaCompany.INOX);
    System.out.println("Available screens at "+ CinemaCompany.INOX);
    for(Screen screen: cinema.getShow()) {
        movieTicketBookingSystem.showScreen(cinema);
        System.out.println("============================");
        System.out.println("Screen: " + screen.getId());
        movieTicketBookingSystem.showTicket(screen);
    }

    Iterator<Screen> ticketIterator = cinema.getShow().iterator();

    movieTicketBookingSystem.bookTicket(movieTicketBookingSystem.getTickets(ticketIterator.next()).getFirst());
    movieTicketBookingSystem.bookTicket(movieTicketBookingSystem.getTickets(ticketIterator.next()).getFirst());
}
}
