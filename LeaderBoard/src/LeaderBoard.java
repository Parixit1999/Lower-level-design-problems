import lombok.Getter;
import lombok.Setter;

class LeaderBoard {

    private List<User> topUser;
    @Getter
    private String gameName;
    private ReentrantReadWriteLock lock;

    public LeaderBoard(String gameName) {
        this.gameName = gameName;
        this.topUser = new ArrayList<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public synchronized void addUser(User user) {
        this.topUser.add(user);
    }

    public void addScore(Player player, long score) {
        player.setScore(score);
    }

    public List<String> getTopK(int k) {

        this.lock.readLock().lock();

        try{
            Collections.sort(topUser);
            List<String> topKId = new LinkedList<>();

            for(int i = 0; i < Math.min(k, this.topUser.size()); i++) {
                User topUser = this.topUser.get(i);
                topKId.add(topUser.getId());
            }

            return topKId;
        } finally {
            this.lock.readLock().unlock();
        }

    }

}
