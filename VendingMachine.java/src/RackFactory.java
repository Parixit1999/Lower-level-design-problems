import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

class RackFactory{
    private int numberOfProducts = 10;

    public Rack creatRack(int rackNumber , int size) {
        this.numberOfProducts = size;
        return new Rack(String.valueOf((char) rackNumber + 97), numberOfProducts);
    }
}
