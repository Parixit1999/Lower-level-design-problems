import java.util.*;
import java.util.concurrent.locks.*;

class Platform {

  private String id;
  private NavigableMap<Integer, Train> scheduledTrain;
  private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
  private TrainDepartureLister trainDepartureLister;

  public Platform(String id) {
    this.id = id;
    this.scheduledTrain = new TreeMap<>();
  }

  public void setTrainDepartureLister(TrainDepartureLister trainDepartureLister) {
    this.trainDepartureLister = trainDepartureLister;
  }

  public boolean assign(Train train) {

    reentrantReadWriteLock.writeLock().lock();

    try{

      if(scheduledTrain.isEmpty()) {
        System.out.println("Train " + train.id + " is scheduled at Platform: " +  this.id);
        scheduledTrain.put(train.getStartTime(), train);
        train.setAssignedPlatform(this);
        return true;
      }

      var previous = scheduledTrain.floorEntry(train.getStartTime());
      var next = scheduledTrain.ceilingEntry(train.getStartTime());

      if(previous != null && train.startTime < previous.getValue().endTime && previous.getValue().startTime < train.endTime) {
        return false;
      }

      if(next != null && train.startTime < next.getValue().endTime && next.getValue().startTime < train.endTime) {
        return false;
      }

      System.out.println("Train " + train.id + " is scheduled at Platform: " +  this.id);
      this.scheduledTrain.put(train.getStartTime(), train);
      train.setAssignedPlatform(this);
      return true;

    } finally {
      reentrantReadWriteLock.writeLock().unlock();
    }
  }

  public void release(Train train) {
    reentrantReadWriteLock.writeLock().lock();

    try{
      this.scheduledTrain.remove(train.startTime);
      if(this.trainDepartureLister != null) {
        this.trainDepartureLister.onDeparture();
      }
    } finally {
      reentrantReadWriteLock.writeLock().unlock();
    }
  }

  public Optional<Train> getTrainForGivenTimestamp(int timestamp) {
    this.reentrantReadWriteLock.readLock().lock();

    try{

      var train = this.scheduledTrain.floorEntry(timestamp);
      if(train != null && train.getValue().getEndTime() >= timestamp) return Optional.of(train.getValue());

      return Optional.empty();
    } finally {
      this.reentrantReadWriteLock.readLock().unlock();
    }
  }
}
