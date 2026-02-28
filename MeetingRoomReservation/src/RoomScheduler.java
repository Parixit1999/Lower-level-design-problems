import java.util.*;

class RoomScheduler {

    private BookingStrategy bookingStrategy;
    private List<Room> roomList;

    public RoomScheduler(List<Room> roomList, BookingStrategy bookingStrategy) {
        this.bookingStrategy = bookingStrategy;
        this.roomList = roomList;
    }

    public boolean scheduleRoom(String username, long startTime, long endTime, long capacity) {
        BookingRequest bookingRequest = new BookingRequest(startTime, endTime, capacity);

        for(Room room: roomList) {
            if(this.bookingStrategy.book(bookingRequest, room)) {
                System.out.println("Room has been book at :" +  room.getRoomName() + ", by " + username);
                return true;
            }
        }

        System.out.println("Rooms are not available for this given time range or there is no room for the given capacity!");
        return false;
    }

    public void addRoom(Room room) {
        this.roomList.add(room);
    }

}
