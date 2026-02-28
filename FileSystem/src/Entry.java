public abstract class Entry {

    private String name;
    private DirectoryNode parent;

    public Entry(String name, DirectoryNode parent) {
        this.name = name;
        this.parent = parent;
    }

    public abstract boolean isDirectory();

    public String getName() {
        return name;
    }

    public DirectoryNode getParent() {
        return parent;
    }
}
