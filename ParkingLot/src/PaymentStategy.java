
import java.util.*;
import java.util.concurrent.*;

interface PaymentStategy{
    boolean pay(Ticket ticket, Long received);
}
