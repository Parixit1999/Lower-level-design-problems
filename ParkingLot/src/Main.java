
import java.util.*;
import java.util.concurrent.*;

public class Main {
 public static void main(String[] args) {

    TicketFactory ticketFactory = new TicketFactory();
    PaymentStategy paymentStategy = new VehiclePaymentStategy();

    CopyOnWriteArrayList parkingSpots = new CopyOnWriteArrayList<>();

    parkingSpots.add(new ParkingSpot("1", ParkingSpotType.REGULAR));
    parkingSpots.add(new ParkingSpot("2", ParkingSpotType.REGULAR));
    parkingSpots.add(new ParkingSpot("3", ParkingSpotType.OVERSIZED));
    parkingSpots.add(new ParkingSpot("4", ParkingSpotType.COMPACT));

    ParkingLot parkingLot = new ParkingLot(parkingSpots);

    ParkingLotManager parkingLotManager = new ParkingLotManager(parkingLot, ticketFactory, paymentStategy);

    Optional<Ticket> ticket1 = parkingLotManager.enterParkingSpot(new Vehicle("1", VehicleType.CAR));
    Optional<Ticket> ticket2 = parkingLotManager.enterParkingSpot(new Vehicle("2", VehicleType.CAR));

    if(ticket1 != null) {
        System.out.println(ticket1.get().vehicle.id);
        System.out.println(ticket1.get().getTotalChargable(System.currentTimeMillis()));
        parkingLotManager.exitParktingLot(ticket1.get(), 9L);
    }



}
}
