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
