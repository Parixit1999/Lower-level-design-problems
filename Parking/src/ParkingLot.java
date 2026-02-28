import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

enum VehicleType {
    MOTORCYCLE,
    CAR,
    TRUCK
}

enum SpotType {
    COMPACT,
    REGULAR,
    OVERSIZED
}

enum PaymentType {
    CARD,
    CASH,
    UPI,
    MOBILE_WALLET
}

abstract class Vehicle {
    private final String licencePlate;
    private final VehicleType type;

    public Vehicle(String licencePlate, VehicleType type) {
        this.licencePlate = licencePlate;
        this.type = type;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public VehicleType getType() {
        return type;
    }
}

class MotorCycle extends Vehicle {
    public MotorCycle(String licencePlate) {
        super(licencePlate, VehicleType.MOTORCYCLE);
    }
}

class Car extends Vehicle {
    public Car(String licencePlate) {
        super(licencePlate, VehicleType.CAR);
    }
}

class Truck extends Vehicle {
    public Truck(String licencePlate) {
        super(licencePlate, VehicleType.TRUCK);
    }
}

class VehicleFactory {
    public static Vehicle createVehicle(String licensePlate, VehicleType type) {
        switch (type) {
            case CAR:
                return new Car(licensePlate);
            case MOTORCYCLE:
                return new MotorCycle(licensePlate);
            case TRUCK:
                return new Truck(licensePlate);
            default:
                throw new IllegalArgumentException("Unsupported vehicle type");
        }
    }
}

abstract class ParkingSpot{

    private String id;
    private SpotType type;
    private boolean isFree = true;
    private Vehicle parkedVehicle;

    public ParkingSpot(String id, SpotType type) {
        this.id = id;
        this.type = type;
    }

    public abstract boolean canFitVehicle(Vehicle vehicle);

    public void park(Vehicle v) {
        this.parkedVehicle = v;
        this.isFree = false;
    }

    public void unpark() {
        this.parkedVehicle = null;
        this.isFree = true;
    }

    public boolean isFree() {
        return isFree;
    }

    public SpotType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }
}



class CompactSpot extends ParkingSpot {
    public CompactSpot(String id){
        super(id, SpotType.COMPACT);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return vehicle.getType() == VehicleType.MOTORCYCLE;
    }
}

class RegularSpot extends ParkingSpot {
    public RegularSpot(String id){
        super(id, SpotType.REGULAR);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return vehicle.getType() == VehicleType.MOTORCYCLE || vehicle.getType() == VehicleType.CAR;
    }
}

class LargeSpot extends ParkingSpot {
    public LargeSpot(String id){
        super(id, SpotType.OVERSIZED);
    }

    @Override
    public boolean canFitVehicle(Vehicle vehicle) {
        return true;
    }
}

class SpotFactory {
    public static ParkingSpot createSpot(String id, SpotType type) throws IllegalAccessException {
        switch (type){
            case COMPACT:
                return new CompactSpot(id);
            case REGULAR:
                return new RegularSpot(id);
            case OVERSIZED:
                return new LargeSpot(id);
            default:
                throw new IllegalAccessException("Unknown spot type");
        }
    }
}

interface PricingStrategy{
    double calculateAmount(long hours);
}

class HourlyPricingStrategy implements  PricingStrategy {
    @Override
    public double calculateAmount(long hours) {
        if(hours < 1) return 10.0;
        return hours * 20.0;
    }
}

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

class TicketFacade {
    NotificationService notificationService;

    public TicketFacade(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public ParkingTicket issueTicket(Vehicle vehicle, ParkingSpot spot) {
        ParkingTicket ticket = new ParkingTicket(vehicle, spot);
        notificationService.notifyObserversForTicketIssued(ticket);
        return ticket;
    }

    public void clearTicket(ParkingTicket ticket) {
        ticket.getSpot().unpark();
        notificationService.notifyObserversForTicketCleared(ticket);
    }
}

interface TicketObserver {
    void onTicketGenerated(ParkingTicket ticket);
    void onTicketCleared(ParkingTicket ticket);
}

class SmsNotificationService implements TicketObserver {
    @Override
    public void onTicketGenerated(ParkingTicket ticket) {
        System.out.println("[SMS] Ticket " + ticket.getTicketId() + " issued for " + ticket.getVehicle().getLicencePlate());
    }

    @Override
    public void onTicketCleared(ParkingTicket ticket) {
        System.out.println("[SMS] Ticket " + ticket.getTicketId() + " cleared for " + ticket.getVehicle().getLicencePlate());
    }
}

class NotificationService {
    private final List<TicketObserver> observers = new ArrayList<>();

    public void addObserver(TicketObserver observer) {
        observers.add(observer);
    }

    public void notifyObserversForTicketIssued(ParkingTicket ticket) {
        for (TicketObserver observer : observers) {
            observer.onTicketGenerated(ticket);
        }
    }

    public void notifyObserversForTicketCleared(ParkingTicket ticket) {
        for (TicketObserver observer : observers) {
            observer.onTicketCleared(ticket);
        }
    }
}

interface PaymentCommand{
    boolean execute();
    void undo();
}

class CardPaymentCommand implements PaymentCommand{

    private double amount;
    private String cardNumber;

    public CardPaymentCommand(double amount, String cardNumber){
        this.amount = amount;
        this.cardNumber = cardNumber;
    }

    public boolean execute(){
        System.out.println("[Bank API] Processing $" + amount + " from card " + cardNumber);
        return true;
    }

    @Override
    public void undo() {
        System.out.println("[Bank API] Refunding $" + amount + " to card " + cardNumber);
    }
}

class CashPaymentCommand implements PaymentCommand {
    private double amount;

    public CashPaymentCommand(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean execute() {
        System.out.println("[Machine] Verifying cash payment of $" + amount);
        return true;
    }

    @Override
    public void undo() {
        System.out.println("[Machine] Dispensing cash refund of $" + amount);
    }
}

class PaymentProcessor {
    private final Stack<PaymentCommand> history = new Stack<>();

    public boolean process(PaymentCommand command) {
        if (command.execute()) {
            history.push(command);
            return true;
        }
        return false;
    }

    public void refundLast() {
        if (!history.isEmpty()) {
            history.pop().undo();
        }
    }
}

class PaymentCommandFactory {
    // We use a "detail" only for types that need it (Card, UPI)
    public static PaymentCommand createCommand(PaymentType type, double amount, String detail) {
        switch (type) {
            case CARD:
                return new CardPaymentCommand(amount, detail);
            case CASH:
                return new CashPaymentCommand(amount); // Detail is ignored here
            default:
                throw new IllegalArgumentException("Unsupported Payment Type");
        }
    }
}