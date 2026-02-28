import lombok.Getter;

interface BookingStrategy{
    boolean book(BookingRequest request, Room room);
}
