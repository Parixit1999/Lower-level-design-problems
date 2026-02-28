enum AuctionStatus{
    ACTIVE,
    ENDED
}


class User {
    String name;

    public User(String name){
        this.name = name;
    }
}

/**
 * This code will make system extendable where we can have multiple type of users.
 */
class Bidder extends User{
    public Bidder(String name) {
        super(name);
    }
}

class Item {
    String itemName;
    long basePrice;

    public Item(String itemName, long basePrice) {
        this.itemName = itemName;
        this.basePrice = basePrice;
    }

    public long getBasePrice() {
        return basePrice;
    }

    public String getItemName() {
        return itemName;
    }
}

class Bid implements Comparable<Bid> {
    Bidder bidder;
    long time;
    long bidPrice;

    public Bid(Bidder bidder, long bidPrice) {
        this.bidPrice = bidPrice;
        this.bidder = bidder;
        this.time = System.currentTimeMillis();
    }

    public Bidder getBidder() {
        return bidder;
    }

    public long getBidPrice(){
        return bidPrice;
    }

    @Override
    public int compareTo(Bid o) {
        return Long.compare(o.getBidPrice(), this.getBidPrice()) ;
    }
}

class Auction {

    String id;
    Item auctionItem;
    LocalDate startDate;
    LocalDate endDate;
    AuctionStatus auctionStatus;
    PriorityQueue<Bid> bids;
    Set<Bidder> bidders;
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Auction(Item auctionItem, LocalDate startDate, LocalDate endDate) {
        this.id = UUID.randomUUID().toString();
        this.auctionItem = auctionItem;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bids = new PriorityQueue<>();
        this.auctionStatus = AuctionStatus.ACTIVE;
        this.bidders = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public boolean registerBidder(Bidder bidder) {
        lock.writeLock().lock();
        try {

            return this.bidders.add(bidder);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean unRegisterBidder(Bidder bidder) {
        lock.writeLock().lock();
        try{

            return this.bidders.remove(bidder);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean addBid (Bid bid) {
        lock.writeLock().lock();
        try{
            /**
             * We can check bid is with in start date and end date.
             */
            if(this.auctionStatus == AuctionStatus.ENDED) {
                System.out.println("Auction is closed!");
                return false;
            }
            /**
             * Check a bigger of the bid and should be registered to this auction.
             */
            return this.bids.add(bid);

        } finally {
            lock.writeLock().unlock();
        }
    }

    protected boolean finishAuction() {
        if(this.auctionStatus == AuctionStatus.ENDED) {
            System.out.println("Auction is already ended!");
            return false;
        }

        this.auctionStatus = AuctionStatus.ENDED;
        return true;
    }

    protected Optional<Bidder> getWinner() {

        lock.readLock().lock();
        try
        {
            if(this.bids.peek() == null) return Optional.empty();

            return Optional.ofNullable(this.bids.peek().getBidder());

        } finally {
            lock.readLock().unlock();
        }
    }
}

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

interface AuctionNotifier{
    void sendNotification(Auction auction);
}

class EmailNotifier implements AuctionNotifier{

    @Override
    public void sendNotification(Auction auction) {
        System.out.println("Email notification: " + auction);
    }
}

void main() {

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
