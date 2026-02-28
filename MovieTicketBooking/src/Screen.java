import java.util.*;

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
