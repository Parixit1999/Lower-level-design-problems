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
