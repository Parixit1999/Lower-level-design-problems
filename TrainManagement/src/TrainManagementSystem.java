import java.util.*;
import java.util.concurrent.locks.*;

class TrainManagementSystem implements TrainDepartureLister {

  private List<Platform> platformList;
  private TrainSchedulingStrategy trainSchedulingStrategy;
  private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private PriorityQueue<Train> waitingQueue;

  public TrainManagementSystem(List<Platform> platformList, TrainSchedulingStrategy schedulingStrategy) {
    this.trainSchedulingStrategy = schedulingStrategy;
    this.platformList = platformList;
    this.waitingQueue = new PriorityQueue<>();

    for(Platform platform: platformList) {
      platform.setTrainDepartureLister(this);
    }
  }

  public void addTrain(Train train) {

    if(!this.trainSchedulingStrategy.schedule(platformList, train)) {
      System.out.println(train.id + " is waiting for the spot!");
      waitingQueue.add(train);
    }
  }

  public void removeTrain(Train train) {
    train.getAssignedPlatform().release(train);
    train.setAssignedPlatform(null);
  }

  public Optional<Train> getTrain(Platform platform, int timestamp) {
    return platform.getTrainForGivenTimestamp(timestamp);
  }

  public Optional<Platform> getPlatform(Train train, int timestamp) {
    if(!(train.getStartTime() <= timestamp && timestamp <= train.getEndTime())) {
      return Optional.empty();
    }

    if(train.getAssignedPlatform() != null) {
      return Optional.of(train.getAssignedPlatform());
    }

    return Optional.empty();
  }

  @Override
  public void onDeparture() {

    lock.writeLock().lock();
    try{

      System.out.println("TMS notified of departure. Processing waiting queue...");

      // Logic to re-check the waiting queue
      while (!waitingQueue.isEmpty()) {
        Train topTrain = waitingQueue.peek();
        if (trainSchedulingStrategy.schedule(platformList, topTrain)) {
          waitingQueue.poll(); // Successfully scheduled!
        } else {
          break; // Still no room for the highest priority train
        }
      }
    } finally {
      lock.writeLock().unlock();
    }
  }
}
