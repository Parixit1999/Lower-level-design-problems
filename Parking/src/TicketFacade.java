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
