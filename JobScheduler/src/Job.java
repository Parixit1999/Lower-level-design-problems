public class Job implements Comparable<Job> {
  private String name;
  private Priority priority;
  private int processTime;

  public Job(String name, Priority priority, int processTime) {
    this.name = name;
    this.priority = priority;
    this.processTime = processTime;
  }

  public String getName() {
    return name;
  }

  public Priority getPriority() {
    return priority;
  }

  public int getProcessTime() {
    return processTime;
  }

  @Override
  public int compareTo(Job o) {
    int result = o.priority.getPriority() - this.priority.getPriority();
    if (result == 0) return o.processTime - this.processTime;

    return result;
  }
}
