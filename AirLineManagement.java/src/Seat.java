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
