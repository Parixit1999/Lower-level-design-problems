import java.util.*;

class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final long startTime;

    public ParkingTicket(Vehicle vehicle, ParkingSpot spot) {
        this.ticketId = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.spot = spot;
        this.startTime = System.currentTimeMillis();
    }

    public double calculateFare(PricingStrategy pricingStrategy) {
        long endTime = System.currentTimeMillis();
        long totalHours = (endTime - startTime) / (3600* 1000);
        return pricingStrategy.calculateAmount(totalHours);
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getTicketId() {
        return ticketId;
    }

    public long getStartTime() {
        return startTime;
    }
}
