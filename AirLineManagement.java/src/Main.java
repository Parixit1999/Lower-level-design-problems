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

public class Main {
public static void main(String[] args) {

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
}
