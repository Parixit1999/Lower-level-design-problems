import java.awt.print.Book;

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
