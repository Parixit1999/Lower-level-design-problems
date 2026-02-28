public enum ProductType {
    COCA_COLA(10),
    SNAKE(5),
    WATER(10),
    SOFT_DRINK(5),
    COCONUT_WATER(10);

    private final int price;

    ProductType(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
