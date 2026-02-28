import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

class OperatonFactory {

    public Operation createOperation(int row, int col, String text, OperationType operationType){
        return new Operation(row, col, text, operationType);
    }
}
