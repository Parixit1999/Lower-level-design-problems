import com.sun.source.tree.Tree;
import lombok.Getter;

public class Main {
public static void main(String[] args) {

  TrainSchedulingStrategy trainSchedulingStrategy = new TrainSchedulingStrategy();
  Platform p1 = new Platform("p1");
  Platform p2 = new Platform("p2");
//  Platform p3 = new Platform("p3");
//  Platform p4 = new Platform("p4");

  TrainManagementSystem trainManagementSystem = new TrainManagementSystem(Arrays.asList(p1, p2), trainSchedulingStrategy);


  Train train1 = new Train("t1", 0, 60);
  Train train2 = new Train("t2", 60, 100);
  Train train3 = new Train("t3", 45, 90);
  Train train4 = new Train("t4", 90, 120);
  Train train5 = new Train("t5", 120, 160);
  Train train6 = new Train("t6", 0, 200);
  Train train7 = new Train("t7", 80, 100);
  Train train8 = new Train("t8", 200, 260);


  trainManagementSystem.addTrain(train1);
  trainManagementSystem.addTrain(train2);
  trainManagementSystem.addTrain(train3);
  trainManagementSystem.addTrain(train4);
  trainManagementSystem.addTrain(train5);
  trainManagementSystem.removeTrain(train2);
  trainManagementSystem.addTrain(train6);
  trainManagementSystem.addTrain(train7);
  trainManagementSystem.addTrain(train8);

  System.out.println(trainManagementSystem.getTrain(p1, 100).get().id);

  System.out.println(trainManagementSystem.getPlatform(train4, 10).isPresent());

}
}
