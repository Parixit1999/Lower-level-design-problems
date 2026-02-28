import java.util.HashMap;
import java.util.Map;

public class DirectoryNode extends Entry{

    private Map<String, Entry> children;

    public DirectoryNode(String name, DirectoryNode parent) {
        super(name, parent);
        this.children = new HashMap<>();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    public Map<String, Entry> getChildren() {
        return children;
    }

    public void addEntry(Entry entry) {
        children.put(entry.getName(), entry);
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }
 }
