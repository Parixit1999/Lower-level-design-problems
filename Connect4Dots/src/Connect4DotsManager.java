import java.util.*;

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
