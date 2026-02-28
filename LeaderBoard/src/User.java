import lombok.Getter;
import lombok.Setter;

class User implements Comparable<User>{

    private String id;
    private List<Player> players;

    public User(String id, List<Player> players) {
        this.id = id;
        this.players = players;
    }

    public long getLatestPoint() {
        long points = 0;
        for(Player p: players) {
            points += p.getScore();
        }
        return points;
    }

    @Override
    public int compareTo(User o) {
        int result =  Long.compare(o.getLatestPoint(), this.getLatestPoint());

        if(result == 0) {
            return this.id.compareTo(o.getId());
        }

        return result;
    }
}
