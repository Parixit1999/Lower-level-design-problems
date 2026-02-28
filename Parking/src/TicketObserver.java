interface TicketObserver {
    void onTicketGenerated(ParkingTicket ticket);
    void onTicketCleared(ParkingTicket ticket);
}
