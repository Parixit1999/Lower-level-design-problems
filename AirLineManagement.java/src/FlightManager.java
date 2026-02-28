import java.util.*;

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
