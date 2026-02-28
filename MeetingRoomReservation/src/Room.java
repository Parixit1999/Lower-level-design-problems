import lombok.Getter;

@Getter
class Room {

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
