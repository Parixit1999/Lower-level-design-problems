import javax.swing.text.html.Option;
import java.util.*;

class Disk {

    private String id;
    private DiskType DiskType;
    private DiskColor DiskColor;

    public Disk(DiskType diskType, DiskColor diskColor) {
        this.id = UUID.randomUUID().toString();
        this.DiskType = diskType;
        this.DiskColor = diskColor;
    }

    public String getId() {
        return id;
    }

    public DiskColor getDiskColor() {
        return DiskColor;
    }

    public DiskType getDiskType() {
        return DiskType;
    }
}
