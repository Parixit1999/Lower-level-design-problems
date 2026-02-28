void main() throws IllegalAccessException {
    NotificationService notifier = new NotificationService();
    notifier.addObserver(new SmsNotificationService());
//    notifier.addObserver(new WhatsappNoficationService());

    TicketFacade ticketFacade = new TicketFacade(notifier);
    PaymentProcessor paymentProcessor = new PaymentProcessor();

    ParkingLot lot = ParkingLot.getInstance(ticketFacade, paymentProcessor);

    lot.addSpot(SpotFactory.createSpot("C1", SpotType.COMPACT));
    lot.addSpot(SpotFactory.createSpot("R1", SpotType.REGULAR));
    lot.addSpot(SpotFactory.createSpot("L1", SpotType.OVERSIZED));

    Vehicle myCar = VehicleFactory.createVehicle("JAVA-2025", VehicleType.CAR);
    ParkingTicket ticket = lot.entry(myCar);

    // Exit
    if (ticket != null) {
        lot.exit(ticket, new HourlyPricingStrategy(), PaymentType.CARD, "4444-5555-6666-7777");
    }
}