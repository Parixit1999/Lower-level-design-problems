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
