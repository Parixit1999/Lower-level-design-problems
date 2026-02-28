interface OrderObserver {
    void onUpdate(String orderId, OrderStatus orderStatus);
}
