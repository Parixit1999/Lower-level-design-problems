interface TrainManagerService{
    String assignPlatform(String trianId, int arrivalTime, int waitTime);
    String getTrainAtPlatform(int platformNumber, int timestamp);
    String getPlatformOfTrain(String trainId, int timestamp);
}
