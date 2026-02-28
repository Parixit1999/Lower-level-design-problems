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
