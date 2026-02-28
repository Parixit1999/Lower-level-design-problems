public enum Priority {
  LOW(1),
  MEDIUM(2),
  HIGH(3);

  private final int priority;
  Priority(int priority) {
    this.priority = priority;
  }

  public int getPriority() {
    return this.priority;
  }

}
