void main() {
        // 1. Setup Infrastructure
        RackFactory rackFactory = new RackFactory();
        PaymentStrategy cashStrategy = new CashPaymentStrategy();
        VendingMachine myMachine = new VendingMachine("TechSnack 5000", 3, rackFactory, cashStrategy);

        // 2. Setup Actors
        Admin admin = new Admin("A001", "Alice");
        User user = new User("U001", "Bob");

        // 3. Admin Action: Stock the Machine
        System.out.println("\n--- Admin: Stocking Machine ---");
        AdminFacade adminFacade = new AdminFacade(myMachine, admin.getRole());

        // Let's add a Coke to the first rack
        Rack firstRack = myMachine.getRackList().get(0);
        Product coke = new Product(ProductType.DRINK, "Coca-Cola", 1.50);
        adminFacade.addProduct(firstRack, coke);

        // Let's add Chips to the second rack
        Rack secondRack = myMachine.getRackList().get(1);
        Product chips = new Product(ProductType.SNACKS, "Potato Chips", 2.00);
        adminFacade.addProduct(secondRack, chips);

        // 4. User Action: Purchase Process
        System.out.println("\n--- User: Purchasing Item ---");
        UserFacade userFacade = new UserFacade(myMachine, user.getRole());

        // Step A: Browse products in the first rack
        List<Product> availableDrinks = userFacade.getProducts(firstRack);
        Product selected = availableDrinks.get(0);

        // Step B: Select the product
        userFacade.selectProduct(selected);

        // Step C: Try to pay with insufficient funds
        System.out.println("Attempting payment of $1.00...");
        userFacade.makePayment(1.00);

        // Step D: Pay with correct amount
        System.out.println("\nAttempting payment of $2.00...");
        boolean success = userFacade.makePayment(2.00);

        if (success) {
            System.out.println("Transaction Complete!");
            System.out.println("Remaining items in Rack 1: " + firstRack.getProductList().size());
        }
}