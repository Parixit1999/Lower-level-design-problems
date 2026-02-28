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
