
import java.util.*;
import java.util.concurrent.*;

enum VehicleType{
    MOTORCYCLE(5),
    CAR(10),
    TRUCK(20);

    int size;
    private VehicleType(int size) {
        this.size = size;
    }
}

enum ParkingSpotType{
    COMPACT(5),
    REGULAR(10),
    OVERSIZED(20);

    int size;
    private ParkingSpotType(int size) {
        this.size = size;
    }
}

enum ParkingSpotStatus{
    AVAILABLE,
    BOOKED
}

interface PaymentStategy{
    boolean pay(Ticket ticket, Long received);
}

class VehiclePaymentStategy implements PaymentStategy{

    static public int CAR_CHARGE = 10;
    static public int MOTORCYCLE_CHARGE = 5;
    static public int TRUCK_CHARGE = 15;
    static public int DURATION_CHARGE = 10;

    @Override
    public boolean pay(Ticket ticket, Long received) {
        Long currentTime = System.currentTimeMillis();
        if(received < ticket.getTotalChargable(currentTime)) {
            return false;
        }
        System.out.print("Total charged: " + ticket.getTotalChargable(currentTime));
        return true;
    }
}

class TicketFactory {
    public Ticket createTicket(Vehicle vehicle, ParkingSpot parkingSpot) {
        return new Ticket(vehicle, parkingSpot);
    }
}

class Ticket{

    Vehicle vehicle;
    ParkingSpot parkingSpot;
    Long startedAt;
    Long endedAt;

    public Ticket(Vehicle vehicle, ParkingSpot parkingSpot) {
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.startedAt = System.currentTimeMillis();
    }

    public Long getTotalChargable(Long currentTime) {

        Long duration = currentTime - startedAt;
        Long totalCharge = duration * VehiclePaymentStategy.DURATION_CHARGE;

        if(vehicle.vehicleType == VehicleType.CAR) {
            totalCharge += VehiclePaymentStategy.CAR_CHARGE;
        } else if(vehicle.vehicleType == VehicleType.MOTORCYCLE) {
            totalCharge += VehiclePaymentStategy.MOTORCYCLE_CHARGE;
        } else{
            totalCharge += VehiclePaymentStategy.TRUCK_CHARGE;
        }

        return totalCharge;
    }
}

class Vehicle{
    String id;
    VehicleType vehicleType;

    public Vehicle(String id, VehicleType vehicleType) {
        this.id = id;
        this.vehicleType = vehicleType;
    }

}

class ParkingSpot{

    String id;
    ParkingSpotType parktingSpotType;
    ParkingSpotStatus parkingSpotStatus;

    public ParkingSpot(String id, ParkingSpotType parkingSoptType) {
        this.id = id;
        this.parktingSpotType = parkingSoptType;
    }

    public synchronized void updateParkingSpotStatus(ParkingSpotStatus newStatus) {
        this.parkingSpotStatus = newStatus;
    }

}

class ParkingLot{

    private CopyOnWriteArrayList<ParkingSpot> parkingSpots;


    public ParkingLot(CopyOnWriteArrayList<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }

    public Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle) {
        for(ParkingSpot parkingSpot: parkingSpots) {
            if(parkingSpot.parktingSpotType.size >= vehicle.vehicleType.size) {
                return Optional.of(parkingSpot);
            }
        }
        return Optional.empty();
    }

}


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

 void main(String[] args) {

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

