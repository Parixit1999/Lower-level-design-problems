
import java.util.*;
import java.util.concurrent.*;

class ParkingLotManager{

    ParkingLot parkingLot;
    TicketFactory ticketFactory;
    PaymentStategy paymentStategy;

    public ParkingLotManager(ParkingLot parkingLot, TicketFactory ticketFactory, PaymentStategy paymentStategy) {
        this.parkingLot = parkingLot;
        this.ticketFactory = ticketFactory;
        this.paymentStategy = paymentStategy;
    }


    public Optional<Ticket> enterParkingSpot(Vehicle vehicle) {
        Optional<ParkingSpot> parkingSpot = this.parkingLot.findAvailableSpot(vehicle);

        if(parkingSpot.get() != null){
            ParkingSpot bookParkingSpot = parkingSpot.get();
            Ticket ticket = ticketFactory.createTicket(vehicle, bookParkingSpot);
            bookParkingSpot.updateParkingSpotStatus(ParkingSpotStatus.BOOKED);
            return Optional.of(ticket);
        } else{
            System.out.println("Not enough spot, try later!");
        }

        return Optional.empty();
    }

    public boolean exitParktingLot(Ticket ticket, Long totalAmount) {
        boolean paymentStatus = this.paymentStategy.pay(ticket, totalAmount);
        if(paymentStatus) {

            System.out.print("Exiting the vechicle!");
            ticket.parkingSpot.updateParkingSpotStatus(ParkingSpotStatus.AVAILABLE);
        } else{
            System.out.print("Please pay enough money");
        }
        return paymentStatus;
    }

}
