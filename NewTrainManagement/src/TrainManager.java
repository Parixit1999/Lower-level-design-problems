import java.awt.print.Book;

class TrainManager implements TrainManagerService{

    public PriorityBlockingQueue<Platform> availablePlatform = new PriorityBlockingQueue<>();
    public PriorityBlockingQueue<BookedPlatform> bookedPlatforms = new PriorityBlockingQueue<>();
    public BookedPlatformFactory bookedPlatformFactory;
    public TrainManagerRegistry platformRegistry;

    public TrainManager(Set<Platform> platformList, BookedPlatformFactory bookedPlatformFactory, TrainManagerRegistry platformRegistry) {
        this.bookedPlatformFactory = bookedPlatformFactory;
        for(Platform platform: platformList) {
            availablePlatform.add(platform);
        }
        this.platformRegistry = platformRegistry;
    }

    @Override
    public String assignPlatform(String trainId, int arrivalTime, int waitTime) {
        if(!availablePlatform.isEmpty()) {
            BookedPlatform bookedPlatform = this.bookedPlatformFactory.bookPlatform(availablePlatform.poll().id, trainId, arrivalTime, arrivalTime + waitTime);
            this.platformRegistry.addTrain(bookedPlatform, arrivalTime);
            bookedPlatforms.add(bookedPlatform);
            return bookedPlatform.platformId;
        } else{
            BookedPlatform previousBookedPlatform = bookedPlatforms.poll();
            long waitingTime = Math.max(arrivalTime - previousBookedPlatform.departureTime, 0);
            System.out.println("Assigned platform: " + previousBookedPlatform.platformId + " " + "waiting time: " + waitingTime);
            BookedPlatform bookedPlatform = this.bookedPlatformFactory.bookPlatform(previousBookedPlatform.platformId, trainId, arrivalTime, arrivalTime + waitingTime + waitTime);
            this.platformRegistry.addTrain(bookedPlatform, arrivalTime + waitingTime);
            bookedPlatforms.add(bookedPlatform);
            return bookedPlatform.platformId;
        }
    }

    @Override
    public String getTrainAtPlatform(int platformNumber, int timestamp) {
        if(this.platformRegistry.platformToTrain.containsKey(platformNumber)) {
            NavigableMap<Long, BookedPlatform> navigableMap = this.platformRegistry.platformToTrain.get(platformNumber);
            BookedPlatform bookedPlatform = navigableMap.floorEntry(Integer.toUnsignedLong(timestamp)).getValue();
            if(bookedPlatform != null) {
                if(bookedPlatform.departureTime >= timestamp && bookedPlatform.arrivalTime <= timestamp) {
                    return bookedPlatform.trainId;
                }
            }
        }

        return "";
    }

    @Override
    public String getPlatformOfTrain(String trainId, int timestamp) {
        if(this.platformRegistry.trainToPlatform.containsKey(trainId)) {
            List<BookedPlatform> bookedPlatformList = this.platformRegistry.trainToPlatform.get(trainId);

            for(BookedPlatform bookedPlatform: bookedPlatformList) {
                if(bookedPlatform.arrivalTime <= timestamp && timestamp <= bookedPlatform.departureTime) {
                    return bookedPlatform.platformId;
                }
            }
        }

        return "";
    }
}
