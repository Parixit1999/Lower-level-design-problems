class SeatFactory {

    public Seat createSeat(SeatType seatType){
        return switch (seatType) {
            case SeatType.NORMAL -> new NormalSeat();
            case SeatType.PREMIUM -> new PremiumSeat();
            case SeatType.VIP -> new VipSeat();
        };
    }
}
