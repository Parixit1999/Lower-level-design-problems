import javax.swing.text.html.Option;
import java.util.*;

enum DiskType {
    CIRCLE,
    DIAMOND,
}

enum DiskColor {
    RED,
    GREEN
}

enum PlayerType {
    HUMAN,
    ROBOT
}

enum GameStatus{
    WIN,
    DRAW,
    PROGRESS
}

class Disk {

    private String id;
    private DiskType DiskType;
    private DiskColor DiskColor;

    public Disk(DiskType diskType, DiskColor diskColor) {
        this.id = UUID.randomUUID().toString();
        this.DiskType = diskType;
        this.DiskColor = diskColor;
    }

    public String getId() {
        return id;
    }

    public DiskColor getDiskColor() {
        return DiskColor;
    }

    public DiskType getDiskType() {
        return DiskType;
    }
}


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

class PlayerFactory {
    public Player createPlayer(PlayerType playerType, String name) throws Exception {
        switch (playerType) {
            case PlayerType.HUMAN -> {
                return new HumanPlayer(name);
            }

            case PlayerType.ROBOT -> {
                return new RobotPlayer(name);
            }
        }

        throw new Exception("Unable to create object of given type");
    }
}

class HumanPlayer extends  Player {
    public HumanPlayer(String name) {
        super(name, PlayerType.HUMAN);
    }
}

class RobotPlayer extends  Player {
    public RobotPlayer(String name) {
        super(name, PlayerType.ROBOT);
    }
}

class BoardRule {

    public boolean isDraw(Connect4DotsBoard board) {

        for(int i = 0; i < board.getRowLength(); i++){
            for(int j = 0; j < board.getColLength(); j++) {
                if(board.getDisk(i, j) != null) return false;
            }
        }

        return true;
    }

    public boolean isWinner(Connect4DotsBoard board, Disk disk) {
        for(int i = 0; i < board.getRowLength(); i++){
            for(int j = 0; j < board.getColLength(); j++) {
                if(this.checkHorizontalWinner(board, disk, i, j)) return true;
                if(this.checkVerticalWinner(board, disk, i, j)) return true;
                if(this.checkDiagonalWinner(board, disk, i, j)) return true;
            }
        }

        return false;
    }

    private boolean checkDiagonalWinner(Connect4DotsBoard board, Disk disk, int startingRow, int startingCol) {
        int k = startingRow;
        int l = startingCol;
        
        for(int i = 0; i < 4; i++){
            if(k < board.getRowLength() && l < board.getColLength() && board.getDisk(startingRow, startingCol) != null && board.getDisk(k, l) != null && board.getDisk(startingRow, startingCol).getDiskColor().equals(board.getDisk(k, l).getDiskColor())) {
                k += 1;
                l += 1;
            } else{
                return  false;
            }
        }
        return true;
    }

    private boolean checkVerticalWinner(Connect4DotsBoard board, Disk disk, int startingRow, int startingCol) {
        int k = startingRow;
        int l = startingCol;

        for(int i = 0; i < 4; i++){
            if(l < board.getColLength() && board.getDisk(startingRow, startingCol) != null && board.getDisk(k, l)!= null && board.getDisk(startingRow, startingCol).getDiskColor().equals(board.getDisk(k, l).getDiskColor())) {
                l += 1;
            } else{
                return  false;
            }
        }
        return true;
    }

    private boolean checkHorizontalWinner(Connect4DotsBoard board, Disk disk, int startingRow, int startingCol) {
        int k = startingRow;
        int l = startingCol;

        for(int i = 0; i < 4; i++){
            if(k < board.getRowLength() && board.getDisk(startingRow, startingCol) != null &&  board.getDisk(k, l)!= null && board.getDisk(startingRow, startingCol).getDiskColor().equals(board.getDisk(k, l).getDiskColor())) {
                k += 1;
            } else{
                return  false;
            }
        }
        return true;
    }
}

class Connect4DotsBoard {
    private int row;
    private int col;
    private Disk[][] board;

    public Connect4DotsBoard(int row, int col) {
        this.row = row;
        this.col = col;
        board = new Disk[row][col];
    }

    public void clearBoard() {
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                board[i][j] = null;
            }
        }
    }

    public void printBoard() {
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){

                if (board[i][j] == null){
                    System.out.print("X ");
                } else{
                    System.out.print(board[i][j].getDiskColor().toString().charAt(0) + " ");
                }
            }
            System.out.println();
        }
    }

    public boolean putDisk(int i, int j, Disk disk) {
        if (board[i][j] != null) {
            System.out.println("This cell is already occupied!");
            return false;
        }
        board[i][j] = disk;
        return true;
    }

    public int getColLength() {
        return col;
    }

    public int getRowLength() {
        return row;
    }

    public Disk getDisk(int row, int col) {
        return this.board[row][col];
    }
}

class Connect4DotsManager {

    private Connect4DotsBoard board;
    private final Map<Player, Disk> playerDiskMap = new HashMap<>();
    private Player currentPlayer;
    private Player watchingPlayer;
    private BoardRule boardRule = new BoardRule();
    private GameStatus gameStatus;
    Scanner scanner = new Scanner(System.in);

    public Connect4DotsManager(Connect4DotsBoard board, Player player1, Player player2, Disk disk1, Disk disk2) {
        this.board = board;

        System.out.println(player1.getName() + " assigned " + disk1.getDiskColor() + " colored disk!" );
        this.playerDiskMap.put(player1, disk1);

        System.out.println(player2.getName() + " assigned " + disk2.getDiskColor() + " colored disk!" );
        this.playerDiskMap.put(player2, disk2);

        this.currentPlayer = player1;
        this.watchingPlayer = player2;
        this.gameStatus = GameStatus.PROGRESS;
    }

    private void switchTurn() {
        Player previousPlayer = this.currentPlayer;
        this.currentPlayer = this.watchingPlayer;
        this.watchingPlayer = previousPlayer;
    }

    public void play() {
        while(true) {
            board.printBoard();

            System.out.println(this.currentPlayer.getName() + " choose position to put your disk. Row followed by column. E.g. 1 3" );

            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if(! board.putDisk(row, col, playerDiskMap.get(this.currentPlayer)))  {
                continue;
            }

            if(boardRule.isWinner(board, playerDiskMap.get(this.currentPlayer))) {

                board.printBoard();
                System.out.println(this.currentPlayer + " won the match!");
                System.out.println("Terminating the game");
                this.gameStatus = GameStatus.WIN;
                break;
            }

            if(boardRule.isDraw(board)) {

                board.printBoard();
                System.out.println("Board is filled up and no one won tha match. It's a draw!");
                System.out.println("Terminating the game");
                this.gameStatus = GameStatus.DRAW;
            }

            this.switchTurn();
        }
    }

    private GameStatus getGameStatus() {
        return this.gameStatus;
    }

    public Optional<Player> getWinner() {
        if(this.gameStatus == GameStatus.WIN) {
            return Optional.ofNullable(this.currentPlayer);
        }

        return Optional.empty();
    }
}

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
