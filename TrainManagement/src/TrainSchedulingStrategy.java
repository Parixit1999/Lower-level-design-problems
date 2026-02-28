import com.sun.source.tree.Tree;
import lombok.Getter;

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
