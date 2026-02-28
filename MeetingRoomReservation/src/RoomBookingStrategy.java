class RoomBookingStrategy implements BookingStrategy {

    @Override
    public boolean book(BookingRequest request, Room room) {

        synchronized (room) {
            if(room.getRoomCapacity() < request.getCapacity()) return false;
            if(room.isOverLap(request.getStarTime(), request.getEndTime())) return false;

            room.scheduleMeeting(request);
            return true;
        }
    }
}
