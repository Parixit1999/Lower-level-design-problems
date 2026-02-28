class AlertNotifierSubscriber extends AsyncSubscriber{

    public AlertNotifierSubscriber() {
        super(1);
    }

    @Override
    protected void process(Message message) {
        System.out.println("Alter notification: " + message.payload);
    }

}
