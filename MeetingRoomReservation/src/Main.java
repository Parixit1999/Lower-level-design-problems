import lombok.Getter;

@Getter
class BookingRequest implements Comparable {

    private long starTime;
    private long endTime;
    private long capacity;

    public BookingRequest(long startTime, long endTime, long capacity) {
        this.starTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
    }


    @Override
    public int compareTo(Object o) {
        return Long.compare(this.getStarTime(), ((BookingRequest) o).getStarTime());
    }
}

interface BookingStrategy{
    boolean book(BookingRequest request, Room room);
}

class RoomBookingStrategy implements BookingStrategy {

    @Override
    public boolean book(BookingRequest request, Room room) {

        synchronized (room) {
            if(room.getRoomCapacity() < request.capacity) return false;
            if(room.isOverLap(request.getStarTime(), request.getEndTime())) return false;

            room.scheduleMeeting(request);
            return true;
        }
    }
}

@Getter
static class Room {

    private String roomName;
    private PriorityBlockingQueue<BookingRequest> scheduledMeetings;
    private List<String> logs;
    private long roomCapacity;

    public Room(String roomName, long roomCapacity, ScheduledExecutorService scheduledExecutorService, int logRetentionDay) {
        this.roomName = roomName;
        this.roomCapacity = roomCapacity;
        this.scheduledMeetings = new PriorityBlockingQueue<>();
        this.logs = new LinkedList<>();
        scheduledExecutorService.scheduleAtFixedRate(this::removeLog, logRetentionDay, logRetentionDay, TimeUnit.DAYS );
    }

    public synchronized boolean isOverLap(long startTime, long endTime) {

        if(scheduledMeetings.isEmpty()) return false;

        for(BookingRequest scheduledMeeting: scheduledMeetings) {
            if(startTime < scheduledMeeting.getEndTime() && scheduledMeeting.getStarTime() < endTime) {
                return true;
            }
        }

        return false;
    }

    public synchronized void scheduleMeeting(BookingRequest request) {
        logs.add("Meeting is scheduled at: " + new Date(System.currentTimeMillis()) + " startime: " + request.starTime + " endtime: " + request.endTime  + " capacity:" + request.capacity);
        this.scheduledMeetings.add(request);
    }

    private synchronized void removeLog() {
        this.logs.clear();
    }
}

class RoomCreationFactory {

    public static Room createRoom(String roomName, long roomCapacity, ScheduledExecutorService scheduledExecutorService, int logRetentionDay) {
        return new Room(roomName, roomCapacity, scheduledExecutorService, logRetentionDay);
    }
}

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

void main() throws InterruptedException {
    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    Room conferenceA = new Room("Conference A", 10,  scheduledExecutorService, 7);
    Room boardRoom = new Room("Board Room", 5, scheduledExecutorService, 7);

    List<Room> rooms = Collections.synchronizedList(new ArrayList<>(Arrays.asList(conferenceA, boardRoom)));
    RoomScheduler scheduler = new RoomScheduler(rooms, new RoomBookingStrategy());

    ExecutorService executor = Executors.newFixedThreadPool(10);
    System.out.println("--- Starting Failure Logic Test ---");

    // FAIL CASE 1: Capacity Impossible
    // No room has 50 seats. Should fail immediately for all rooms.
    executor.execute(() -> scheduler.scheduleRoom("User_Too_Big", 1000, 1100, 50));

    // FAIL CASE 2: Total Saturation
    // We book both rooms for the same time.
    scheduler.scheduleRoom("Blocker_1", 1400, 1500, 2); // Takes Room A
    scheduler.scheduleRoom("Blocker_2", 1400, 1500, 2); // Takes Room B

    // This user should now FAIL because both rooms are occupied at 14:00.
    executor.execute(() -> scheduler.scheduleRoom("User_No_Space_Left", 1415, 1445, 2));

    // FAIL CASE 3: The Overlap/Sandwich Saturation
    // Room A is busy 09:00-10:00. Room B is busy 11:00-12:00.
    scheduler.scheduleRoom("Blocker_A", 900, 1000, 1);
    scheduler.scheduleRoom("Blocker_B", 1100, 1200, 1);

    // This user wants 08:00-13:00.
    // It overlaps with Room A's meeting AND Room B's meeting.
    // Should fail for both rooms.
    executor.execute(() -> scheduler.scheduleRoom("User_Mega_Sandwich", 800, 1300, 1));

    executor.shutdown();
    executor.awaitTermination(5, TimeUnit.SECONDS);
    System.out.println("--- Test Complete ---");
}
