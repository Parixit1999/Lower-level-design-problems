class OperatonFactory {

    public Operation createOperation(int row, int col, String text, OperationType operationType){
        return new Operation(row, col, text, operationType);
    }
}
