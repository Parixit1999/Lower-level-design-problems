//The airline management system should allow users to search for flights based on source, destination, and date.
//Users should be able to book flights, select seats, and make payments.
//The system should manage flight schedules, aircraft assignments, and crew assignments.
//The system should handle passenger information.
//The system should support different types of users, such as passengers, airline staff, and administrators.

//The airline management system should allow users to search for flights based on source, destination, and date.
//Users should be able to book flights, select seats.
//The system should manage flight schedules, aircraft assignments, and crew assignments.
//The system should handle passenger information.
//The system should support different types of users, such as passengers, airline staff, and administrators.

import lombok.Getter;

enum  User {
    CLIENT,
    PASSENGER,
    AIRLINE_STAFF,
    ADMINISTRATOR
}

enum AirCraftType{
    BOING,
    AIRBUS
}

enum SeatType{
    BUSINESS,
    ECONOMY
}

@Getter
class AirCraft {
    private String id;
    private AirCraftType airCraftType;
    private int seatSize;

    private AirCraft(String id, AirCraftType airCraftType, int seatSize){
        this.airCraftType = airCraftType;
        this.id = id;
        this.seatSize = seatSize;
    }
}

class BoingAirCraft extends AirCraft{

        public BoingAirCraft(String id, int seatSize) {
            super(id, AirCraftType.BOING, seatSize);
        }
}

class AirBusAirCraft extends AirCraft{
    public AirBusAirCraft(String id, int seatSize) {
        super(id, AirCraftType.AIRBUS, seatSize);
    }
}


@Getter
class Passenger {
    private String id;
    private String name;
    private User userType;
    private Seat seat;

    public Passenger(String id, String name) {
        this.id = id;
        this.name = name;
        this.userType = User.CLIENT;
    }

    public void assignSeat(Seat seat) {
        this.seat = seat;
    }
}

@Getter
class Seat {
    private String id;
    private Flight flight;
    private SeatType seatType;
    private boolean isBooked;

    public Seat(String id, Flight flight, SeatType seatType) {
        this.flight = flight;
        this.isBooked = false;
        this.id = id;
        this.seatType = seatType;
    }

    public boolean bookSeat() {
        if(this.isBooked) return false;

        this.isBooked = true;
        return this.isBooked;
    }
}


@Getter
class Flight {

    private String id;
    private String name;
    private String source;
    private String destination;
    private long date;
    private AirCraft airCraft;
    private List<Seat> seats;

    public Flight(String id, String name, AirCraft airCraft, String source, String destination, long date) {
        this.name = name;
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.date = date;
        this.airCraft = airCraft;
        this.seats = new ArrayList<>(this.airCraft.getSeatSize());
    }

    public void changeAirCraft(AirCraft airCraft) {
        this.airCraft = airCraft;
    }

    public void addSeat(Seat seat) {
        this.seats.add(seat);
    }

    public List<Seat> getSeats() {
        return this.seats;
    }

    public List<Seat> getAvailableSeats(){
        List<Seat> availableSeats = new ArrayList<>();

        for(Seat seat: this.seats){
            if(!seat.isBooked()) {
                availableSeats.add(seat);
            }
        }

        return availableSeats;
    }
}

class FlightManager {

    private HashMap<String, List<Flight>> flightMapping;

    public FlightManager() {
        flightMapping = new HashMap<>();
    }

    public void addFlight(Flight flight) {
        if(flightMapping.containsKey(flight.getSource())) {
            flightMapping.get(flight.getSource()).add(flight);
        } else{
            flightMapping.put(flight.getSource(), new ArrayList<>(List.of(flight)));
        }
    }

    public List<Flight> searchFlight(String source, String destination, long date) {
        List<Flight> retrivedFlights = new ArrayList<>();

        for(Flight flight: flightMapping.get(source)) {
            if(flight.getDestination().equals(destination) && flight.getDate() == date) {
                retrivedFlights.add(flight);
            }
        }

        return retrivedFlights;
    }

    public void bookFlight(Seat seat, Passenger passenger) {
        if(seat.bookSeat()) {
            passenger.assignSeat(seat);
            System.out.println("Passenger " + passenger.getName() + " booked the flight " + seat.getFlight().getName() + " from " + seat.getFlight().getSource() + " to " + seat.getFlight().getSource());
            System.out.println("Seat No: " + seat.getId());
        } else {
            System.out.println("Seat is already booked!");
        }
    }
}


void main() {

    BoingAirCraft boingAirCraft = new BoingAirCraft("1", 10);
    Passenger passenger1 = new Passenger("1", "Parixit");
    Passenger passenger2 = new Passenger("2", "Sanghani");

    Flight flight1 = new Flight("1", "Xo1", boingAirCraft, "Mumbai", "Boston", 10);
    Flight flight2 = new Flight("2", "Xo2", boingAirCraft, "Boston", "California", 13);
    Flight flight3 = new Flight("3", "Xo3", boingAirCraft, "Mumbai", "Boston", 10);

    for(int i = 0; i < 5; i++) {
        Seat seat = new Seat(String.valueOf(i), flight1, SeatType.ECONOMY);
        flight1.addSeat(seat);
    }

    for(int i = 0; i < 5; i++) {
        Seat seat = new Seat(String.valueOf(i), flight2, SeatType.ECONOMY);
        flight2.addSeat(seat);
    }

    for(int i = 0; i < 5; i++) {
        Seat seat = new Seat(String.valueOf(i), flight3, SeatType.ECONOMY);
        flight3.addSeat(seat);
    }


    FlightManager airCraftManagement = new FlightManager();

    airCraftManagement.addFlight(flight1);
    airCraftManagement.addFlight(flight2);
    airCraftManagement.addFlight(flight3);

    System.out.println(airCraftManagement.searchFlight("Mumbai", "Boston", 10));
    System.out.println(airCraftManagement.searchFlight("Mumbai", "Boston", 0));

    airCraftManagement.bookFlight(flight1.getSeats().getFirst(), passenger1);
    airCraftManagement.bookFlight(flight2.getSeats().getFirst(), passenger1);

    airCraftManagement.bookFlight(flight1.getSeats().getFirst(), passenger1);

    airCraftManagement.bookFlight(flight1.getSeats().getLast(), passenger1);

    System.out.println("============");
    System.out.println("Available seats");
    for(Seat seats: flight1.getAvailableSeats()) {
        System.out.println(seats.getId());
    }

    System.out.println("============");

}
