public class Main {
public static void main(String[] args) {

    Bidder bidder1 = new Bidder("Parixit");
    Bidder bidder2 = new Bidder("Sagar");
    Bidder bidder3 = new Bidder("Rony");
    Bidder bidder4 = new Bidder("Manan");

    Item tv1 = new Item("SumsungTV", 150);
    Item tv2 = new Item("SonyTV", 320);
    Item fridge1 = new Item("Fridge1", 100);
    Item fridge2 = new Item("Fridge2", 100);

    AuctionManager manager = new AuctionManager();

    Auction tv1Auction = manager.createAuction(tv1, LocalDate.now(), LocalDate.now());
    Auction tv2Auction = manager.createAuction(tv2, LocalDate.now(), LocalDate.now());
    Auction fridge1Auction = manager.createAuction(fridge1, LocalDate.now(), LocalDate.now());

    manager.getActiveAuction();

    tv1Auction.registerBidder(bidder1);
    tv1Auction.registerBidder(bidder2);

    tv1Auction.addBid(new Bid(bidder1, 100));
    tv1Auction.addBid(new Bid(bidder2, 200));

    System.out.println(manager.endAuction(tv1Auction.getId()));

    System.out.println(tv1Auction.getWinner().get().name);


}
}
