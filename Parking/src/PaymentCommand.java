interface PaymentCommand{
    boolean execute();
    void undo();
}
