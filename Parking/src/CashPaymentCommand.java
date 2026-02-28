class CashPaymentCommand implements PaymentCommand {
    private double amount;

    public CashPaymentCommand(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean execute() {
        System.out.println("[Machine] Verifying cash payment of $" + amount);
        return true;
    }

    @Override
    public void undo() {
        System.out.println("[Machine] Dispensing cash refund of $" + amount);
    }
}
