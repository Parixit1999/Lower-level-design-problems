class Train implements Comparable<Train>{

  String id;
  int startTime;
  int endTime;
  Platform assignedPlatform;

  public Train(String id, int startTime, int endTime) {
    this.id = id;
    this.startTime = startTime;
    this.endTime = endTime;
    this.assignedPlatform = null;
  }

  public synchronized void setAssignedPlatform(Platform platform) {
    this.assignedPlatform = platform;
  }

  public String getId() {
    return id;
  }

  public int getStartTime() {
    return startTime;
  }

  public int getEndTime() {
    return endTime;
  }

  public Platform getAssignedPlatform() {
    return assignedPlatform;
  }

  @Override
  public int compareTo(Train o) {
    return Integer.compare(this.startTime, o.startTime);
  }
}
