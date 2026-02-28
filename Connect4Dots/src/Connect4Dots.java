import javax.swing.text.html.Option;
import java.util.*;

public class Connect4Dots {

    void main() throws Exception {
        Connect4DotsBoard board = new Connect4DotsBoard(5, 6);
        PlayerFactory playerFactory = new PlayerFactory();
        Connect4DotsManager connect4DotsManager;

        Player player1 = playerFactory.createPlayer(PlayerType.HUMAN, "Parixit");
        Player player2 = playerFactory.createPlayer(PlayerType.HUMAN, "Keyur");

        Disk disk1 = new Disk(DiskType.CIRCLE, DiskColor.GREEN);
        Disk disk2 = new Disk(DiskType.CIRCLE, DiskColor.RED);

        connect4DotsManager = new Connect4DotsManager(board, player1, player2, disk1, disk2);
        connect4DotsManager.play();

        System.out.println(connect4DotsManager.getWinner());

    }
}
