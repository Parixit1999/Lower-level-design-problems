import java.awt.print.Book;

interface TrainManagerService{
    String assignPlatform(String trianId, int arrivalTime, int waitTime);
    String getTrainAtPlatform(int platformNumber, int timestamp);
    String getPlatformOfTrain(String trainId, int timestamp);
}

class Train {
    String id;

    public Train(String id) {
        this.id = id;
    }

}

class Platform implements Comparable<Platform>{
    String id;

    public Platform(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(Platform o) {
        return this.id.compareTo(o.id);
    }
}

class BookedPlatformFactory{
    public BookedPlatform bookPlatform(String platformId, String trainId, long arrivalTime, long departureTime) {
        return new BookedPlatform(platformId, trainId, arrivalTime, departureTime);
    }
}

class BookedPlatform implements Comparable<BookedPlatform>{
    String platformId;
    String trainId;
    long departureTime;
    long arrivalTime;

    public BookedPlatform (String platformId, String trainId, long arrivalTime, long departureTime) {
        this.platformId = platformId;
        this.trainId = trainId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    @Override
    public int compareTo(BookedPlatform o) {
        int result = Long.compare(this.departureTime, o.departureTime);
        if(result == 0) {
            return this.platformId.compareTo(o.platformId);
        }

        return result;
    }
}

class TrainManagerRegistry {

    public HashMap<String, NavigableMap<Long, BookedPlatform>> platformToTrain;
    public HashMap<String, List<BookedPlatform>> trainToPlatform;

    public TrainManagerRegistry(){
        this.platformToTrain = new HashMap<>();
        this.trainToPlatform = new HashMap<>();
    }

    public void addTrain(BookedPlatform bookedPlatform, long arrivalTime) {
        this.platformToTrain.compute(bookedPlatform.platformId, (key, value) -> {
            if(value == null){
                value = new TreeMap<>();
            }

            value.put(arrivalTime, bookedPlatform);
            return value;
        });

        this.trainToPlatform.compute(bookedPlatform.trainId, (key , value) -> {
            if(value == null) {
                value = new ArrayList<>();
            }
            value.add(bookedPlatform);
            return value;
        });
    }


}

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

void main() {

}
