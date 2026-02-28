import java.util.*;

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
