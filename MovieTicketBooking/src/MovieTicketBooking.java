import java.util.*;

enum SeatType {
    NORMAL,
    PREMIUM,
    VIP
}

enum CinemaCompany {
    INOX,
    AMX,
    ALPHA
}

class SeatFactory {

    public Seat createSeat(SeatType seatType){
        return switch (seatType) {
            case SeatType.NORMAL -> new NormalSeat();
            case SeatType.PREMIUM -> new PremiumSeat();
            case SeatType.VIP -> new VipSeat();
        };
    }
}

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

class NormalSeat extends Seat {

    public NormalSeat() {
        super(SeatType.NORMAL);
    }

}

class PremiumSeat extends Seat {

    public PremiumSeat() {
        super(SeatType.PREMIUM);
    }

}

class VipSeat extends Seat {

    public VipSeat() {
        super(SeatType.VIP);
    }

}

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

class Screen {

    private String id;
    private Room room;
    private Movie movie;
    private long startTime;
    private long endTime;
    private List<Ticket> ticketList = new ArrayList<>();

    public Screen(Movie movie, Room room, long startTime, long endTime) {
        this.id = UUID.randomUUID().toString();
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;

        for(Seat seat: room.getSeats()) {
            ticketList.add(new Ticket(room, seat));
        }
    }

    public String getId() {
        return id;
    }

    public Movie getMovie() {
        return movie;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public Room getRoom(){
        return this.room;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }
}

class Movie {
    private String id;
    private String movieName;

    public Movie(String movieName) {
        this.id = UUID.randomUUID().toString();
        this.movieName = movieName;
    }

    public String getId() {
        return id;
    }

    public String getMovieName() {
        return movieName;
    }
}

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

interface PricingStrategy{
    double getFare();
}

class NormalPricingStrategy implements PricingStrategy {

    private final static double fare = 10.0;

    @Override
    public double getFare() {
        return fare;
    }
}

class PremiumPricingStategy implements PricingStrategy {

    private final static double fare = 20.0;

    @Override
    public double getFare() {
        return fare;
    }
}

class VIPPricingStrategy implements PricingStrategy {

    private final static double fare = 30.0;

    @Override
    public double getFare() {
        return fare;
    }
}

class PricingStrategyFactory {
    public PricingStrategy createPricingStrategy(SeatType seatType){
        System.out.println(seatType);
        System.out.println(seatType == SeatType.NORMAL);
        switch (seatType) {
            case SeatType.NORMAL -> {
                return new NormalPricingStrategy();
            }
            case SeatType.PREMIUM -> {
                return new PremiumPricingStategy();
            }
            case SeatType.VIP -> {
                return new VIPPricingStrategy();
            }
        }

        throw new RuntimeException("Invalid seat type!");
    }
}


class Cinema {

    private String id;
    private CinemaCompany name;
    private HashSet<Screen> screens = new HashSet<>();
    private HashSet<Room> rooms = new HashSet<>();
    private SeatFactory seatFactory = new SeatFactory();

    public Cinema(CinemaCompany name, int roomNumber) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        for(int i = 0; i < roomNumber; i++) {
            Room newRoom = new Room();
            for(int j = 0; j < 30; j++) newRoom.addSeat(seatFactory.createSeat(SeatType.NORMAL));
            for(int j = 0; j < 20; j++) newRoom.addSeat(seatFactory.createSeat(SeatType.PREMIUM));
            for(int j = 0; j < 30; j++) newRoom.addSeat(seatFactory.createSeat(SeatType.VIP));
            rooms.add(newRoom);
        }
    }

    public String getId() {
        return id;
    }

    public CinemaCompany getName() {
        return name;
    }

    public void addShow(Movie movie, long startTime, long endTime, long l){
        cleanScheduleSet();
        Room room = rooms.stream().iterator().next();
        if (room == null) {
            throw new RuntimeException("No room available to schedule a show!");
        }
        this.screens.add(new Screen(movie, room, startTime, endTime));
    }

    private void cleanScheduleSet() {
        HashSet<Screen> removeScreens = new HashSet<>();
        for(Screen screen: screens) {
            if(screen.getEndTime() < System.currentTimeMillis()) {
                rooms.add(screen.getRoom());
                removeScreens.add(screen);
            }
        }

        for(Screen screen: removeScreens) {
            screens.remove(screen);
        }
    }

    public HashSet<Screen> getShow() {
        return screens;
    }
}

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


interface Notifier{
    void sendMessage(Ticket ticket);
}

class EmailNotifier implements Notifier{
    @Override
    public void sendMessage(Ticket ticket) {
        System.out.println("Sending ticket: " + ticket.getId() + " to Email!");
    }
}

class WhatsAppNotifier implements Notifier{
    @Override
    public void sendMessage(Ticket ticket) {
        System.out.println("Sending ticket: " + ticket.getId() + " to WhatsApp!");
    }
}
