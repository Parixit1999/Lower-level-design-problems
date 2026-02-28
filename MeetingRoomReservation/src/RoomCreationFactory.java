import lombok.Getter;

class RoomCreationFactory {

    public static Room createRoom(String roomName, long roomCapacity, ScheduledExecutorService scheduledExecutorService, int logRetentionDay) {
        return new Room(roomName, roomCapacity, scheduledExecutorService, logRetentionDay);
    }
}
