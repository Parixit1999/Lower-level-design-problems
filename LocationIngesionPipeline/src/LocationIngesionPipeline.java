import java.util.concurrent.*;

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
