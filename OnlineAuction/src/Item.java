class Item {
    String itemName;
    long basePrice;

    public Item(String itemName, long basePrice) {
        this.itemName = itemName;
        this.basePrice = basePrice;
    }

    public long getBasePrice() {
        return basePrice;
    }

    public String getItemName() {
        return itemName;
    }
}
