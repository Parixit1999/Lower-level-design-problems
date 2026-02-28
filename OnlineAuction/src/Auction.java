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
