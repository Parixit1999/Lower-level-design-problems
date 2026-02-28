import java.util.*;

abstract class Player {
    private String id;
    private String name;
    private PlayerType playerType;

    public Player(String name, PlayerType playerType) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.playerType = playerType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }
}
