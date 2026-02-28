class EmailNotifier implements AuctionNotifier{

    @Override
    public void sendNotification(Auction auction) {
        System.out.println("Email notification: " + auction);
    }
}
