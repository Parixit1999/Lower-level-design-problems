class AuctionManager {

    HashMap<String, Auction> auctionMap;

    public AuctionManager(){
        this.auctionMap = new HashMap<>();
    }

    public Auction createAuction(Item item, LocalDate startTime, LocalDate endTime) {
        Auction auction = new Auction(item, startTime, endTime);
        this.auctionMap.put(auction.id, auction);
        return auction;
    }

    public List<String> getActiveAuction(){
        return this.auctionMap.values().stream().filter(a -> a.auctionStatus == AuctionStatus.ACTIVE).map(Auction::getId).toList();
    }

    public boolean endAuction(String auctionId) {
        if(auctionMap.containsKey(auctionId)) {
            auctionMap.get(auctionId).finishAuction();
            return true;
        }
        return false;
    }
}
