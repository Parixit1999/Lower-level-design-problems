class Task implements Comparable<Task> {

    private String name;
    private long startTime;
    private long interval;

    public Task(String name, long startTime, long interval) {
        this.name = name;
        this.startTime = startTime;
        this.interval = interval;
    }

    public String getName() {
        return name;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getInterval() {
        return interval;
    }

    @Override
    public int compareTo(Task o) {
        return Long.compare(this.startTime, o.getStartTime());
    }
}
