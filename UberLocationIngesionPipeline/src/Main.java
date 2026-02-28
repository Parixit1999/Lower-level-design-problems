
import java.util.*;
import java.util.concurrent.*;

enum DriverStatus{
    OFFLINE,
    AVAILABLE,
    ON_TRIP,
    EN_ROUTE
}

class Driver{
    String id;
    DriverStatus driverStatus;

    public Driver(String id) {
        this.id = id;
        this.driverStatus = DriverStatus.AVAILABLE;
    }
}

class Location {
    Long lat;
    Long lg;

    @Override
    public boolean equals(Object obj) {
        return this.lat == ((Location) obj).lat && this.lg == ((Location) obj).lg;
    }
}

interface MessageNotifier{
    void onUpdate(Message message);
}

class Message{
    String id;

    public Message() {
        this.id = UUID.randomUUID().toString();;
    }
}

class LocationUpdate extends Message{
    Driver driver;
    Location location;
    long time;

    public LocationUpdate(Driver driver, Location location, long time) {
        super();
        this.driver = driver;
        this.location = location;
        this.time = time;
    }
}

class DriverUpdateFacotry{
    public static DriverUpdate createDriverUpdate(Long time, Location location) {
        return new DriverUpdate(time, location);
    }
}

static class DriverUpdate{
    Long time;
    Location location;

    public DriverUpdate(Long time, Location location) {
        this.time = time;
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        DriverUpdate compareObject = ((DriverUpdate) obj);

        return compareObject.location.equals(this.location) || compareObject.time.equals(this.time);
    }
}

class Denoiser{
    private ConcurrentHashMap<String, DriverUpdate> driverLatestUpdate = new ConcurrentHashMap<>();

    public boolean denoise(String driverId, DriverUpdate update) {

        DriverUpdate driverUpdate = driverLatestUpdate.compute(driverId, (key, value)-> {
            if(value != null) {
                if(value.equals(update)) {
                    return value;
                } else{
                    return update;
                }
            } else{
                return update;
            }
        });

        return !driverUpdate.equals(update) ;
    }
}



class TripService implements MessageNotifier{

    @Override
    public void onUpdate(Message message) {
        LocationUpdate locationMessage = (LocationUpdate) message;

        System.out.print("Location update: " + locationMessage.driver.id + " " + locationMessage.location + " at: " + new Date(locationMessage.time));
    }

}

interface LocationIngesionOperations{
    void process(LocationUpdate update);
}

class LocationIngesionPipeline implements LocationIngesionOperations{
    private BlockingQueue<LocationUpdate> queue = new LinkedBlockingQueue<>(10000);
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    Denoiser denoiser;
    TripService tripService;

    public LocationIngesionPipeline(Denoiser denoiser, TripService tripService) {
        this.denoiser = denoiser;
        this.tripService = tripService;
        executorService.submit(()-> this.updateService());
    }

    @Override
    public void process(LocationUpdate update) {
        if(!this.denoiser.denoise(update.driver.id, DriverUpdateFacotry.createDriverUpdate(update.time, update.location))) {
            this.queue.add(update);
            executorService.notifyAll();
        }
    }

    private void updateService(){
        try{

            while(true){
                LocationUpdate locationUpdate =  this.queue.take();
                if(locationUpdate.driver.driverStatus.equals(DriverStatus.ON_TRIP)) {
                    this.tripService.onUpdate(locationUpdate);
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}



void main(String[] args) {

}