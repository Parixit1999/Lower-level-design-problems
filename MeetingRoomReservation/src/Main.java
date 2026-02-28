import lombok.Getter;

public class Main {
public static void main(String[] args) throws InterruptedException {
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
}
