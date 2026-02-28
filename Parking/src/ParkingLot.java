import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

class ParkingLot {
    private static ParkingLot instance;
    private List<ParkingSpot> spots = new ArrayList<>();
    private final TicketFacade ticketFacade;
    private final PaymentProcessor paymentProcessor;

    public ParkingLot(TicketFacade ticketFacade, PaymentProcessor paymentProcessor) {
        this.ticketFacade = ticketFacade;
        this.paymentProcessor = paymentProcessor;

    }

    public static synchronized ParkingLot getInstance(TicketFacade ticketFacade, PaymentProcessor paymentProcessor) {
        if(instance == null) instance = new ParkingLot(ticketFacade, paymentProcessor);
        return instance;
    }

    public void addSpot(ParkingSpot parkingSpot){
        spots.add(parkingSpot);
    }

    public synchronized ParkingTicket entry(Vehicle vehicle) {
        for(ParkingSpot spot: spots) {
            if (spot.isFree() && spot.canFitVehicle(vehicle)){
                spot.park(vehicle);
                System.out.println("Vehicle " + vehicle.getLicencePlate() + " parked in " + spot.getId());
                return this.ticketFacade.issueTicket(vehicle, spot);
            }
        }
        System.out.println("No spot available for: " + vehicle.getLicencePlate());
        return null;
    }

    public synchronized void exit(ParkingTicket ticket, PricingStrategy strategy, PaymentType type, String paymentDetail) {
        double fee = ticket.calculateFare(strategy);
        boolean isPaid = this.paymentProcessor.process(PaymentCommandFactory.createCommand(type, fee, paymentDetail));
        if(isPaid){
            this.ticketFacade.clearTicket(ticket);
            System.out.println("Ticket " + ticket.getSpot().getId() + " cleared. Fee Paid: $" + fee);

        } else {
            System.out.println("Payment failed! Gate will remain closed.");
        }
    }
}
