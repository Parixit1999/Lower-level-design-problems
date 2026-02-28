class Client implements OrderObserver {
    String name;

    public Client(String name) {
        this.name = name;
    }

    @Override
    public void onUpdate(String orderId, OrderStatus orderStatus) {

        System.out.println("Client: Order: " + orderId + " status changed to: " + orderStatus);
    }
}
