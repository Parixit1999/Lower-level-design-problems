import java.util.*;

class TrainSchedulingStrategy {

  public boolean schedule(List<Platform> platformList, Train train) {

    for(Platform platform: platformList) {
      if(platform.assign(train)) {
        return true;
      }
    }
    return false;
  }
}
