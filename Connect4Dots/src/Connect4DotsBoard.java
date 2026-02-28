import javax.swing.text.html.Option;
import java.util.*;

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
