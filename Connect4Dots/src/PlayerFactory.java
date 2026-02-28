class PlayerFactory {
    public Player createPlayer(PlayerType playerType, String name) throws Exception {
        switch (playerType) {
            case PlayerType.HUMAN -> {
                return new HumanPlayer(name);
            }

            case PlayerType.ROBOT -> {
                return new RobotPlayer(name);
            }
        }

        throw new Exception("Unable to create object of given type");
    }
}
