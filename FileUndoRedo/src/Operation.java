import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

class Operation {

    int row;
    int col;
    String text;
    OperationType operationType;

    public Operation(int row, int col, String text, OperationType operationType) {
        this.row = row;
        this.col = col;
        this.text = text;
        this.operationType =operationType;
    }
}
