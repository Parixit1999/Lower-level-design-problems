class NewsFeedNotifierSubscriber extends AsyncSubscriber{

    public NewsFeedNotifierSubscriber() {
        super(1);
    }

    @Override
    protected void process(Message message) {
        System.out.println("Alter notification: " + message.payload);
    }
}
