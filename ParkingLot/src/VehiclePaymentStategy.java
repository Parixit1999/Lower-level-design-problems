
import java.util.*;
import java.util.concurrent.*;

class VehiclePaymentStategy implements PaymentStategy{

    static public int CAR_CHARGE = 10;
    static public int MOTORCYCLE_CHARGE = 5;
    static public int TRUCK_CHARGE = 15;
    static public int DURATION_CHARGE = 10;

    @Override
    public boolean pay(Ticket ticket, Long received) {
        Long currentTime = System.currentTimeMillis();
        if(received < ticket.getTotalChargable(currentTime)) {
            return false;
        }
        System.out.print("Total charged: " + ticket.getTotalChargable(currentTime));
        return true;
    }
}
