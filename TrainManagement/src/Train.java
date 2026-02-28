import com.sun.source.tree.Tree;
import lombok.Getter;

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
