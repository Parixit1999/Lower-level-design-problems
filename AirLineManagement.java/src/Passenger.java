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

    public String getName() {
        return name;
    }
}
