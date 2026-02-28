import java.util.*;

class TripService implements MessageNotifier{

    @Override
    public void onUpdate(Message message) {
        LocationUpdate locationMessage = (LocationUpdate) message;

        System.out.print("Location update: " + locationMessage.driver.id + " " + locationMessage.location + " at: " + new Date(locationMessage.time));
    }

}
