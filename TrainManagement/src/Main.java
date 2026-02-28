import com.sun.source.tree.Tree;
import lombok.Getter;

class TrainSchedulingStrategy {

  public boolean schedule(List<Platform> platformList, Train train) {

    for(Platform platform: platformList) {
      if(platform.assign(train)) {
        return true;
      }
    }
    return false;
  }
}

@Getter
class Train implements Comparable<Train>{

  private String id;
  private int startTime;
  private int endTime;
  private Platform assignedPlatform;

  public Train(String id, int startTime, int endTime) {
    this.id = id;
    this.startTime = startTime;
    this.endTime = endTime;
    this.assignedPlatform = null;
  }

  public synchronized void setAssignedPlatform(Platform platform) {
    this.assignedPlatform = platform;
  }

  @Override
  public int compareTo(Train o) {
    return Integer.compare(this.startTime, o.startTime);
  }
}

interface TrainDepartureLister {
  void onDeparture();
}

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


void main() {

  TrainSchedulingStrategy trainSchedulingStrategy = new TrainSchedulingStrategy();
  Platform p1 = new Platform("p1");
  Platform p2 = new Platform("p2");
//  Platform p3 = new Platform("p3");
//  Platform p4 = new Platform("p4");

  TrainManagementSystem trainManagementSystem = new TrainManagementSystem(Arrays.asList(p1, p2), trainSchedulingStrategy);


  Train train1 = new Train("t1", 0, 60);
  Train train2 = new Train("t2", 60, 100);
  Train train3 = new Train("t3", 45, 90);
  Train train4 = new Train("t4", 90, 120);
  Train train5 = new Train("t5", 120, 160);
  Train train6 = new Train("t6", 0, 200);
  Train train7 = new Train("t7", 80, 100);
  Train train8 = new Train("t8", 200, 260);


  trainManagementSystem.addTrain(train1);
  trainManagementSystem.addTrain(train2);
  trainManagementSystem.addTrain(train3);
  trainManagementSystem.addTrain(train4);
  trainManagementSystem.addTrain(train5);
  trainManagementSystem.removeTrain(train2);
  trainManagementSystem.addTrain(train6);
  trainManagementSystem.addTrain(train7);
  trainManagementSystem.addTrain(train8);

  System.out.println(trainManagementSystem.getTrain(p1, 100).get().id);

  System.out.println(trainManagementSystem.getPlatform(train4, 10).isPresent());

}
