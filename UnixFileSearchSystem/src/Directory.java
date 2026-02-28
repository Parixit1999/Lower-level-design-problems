//Design Unix File Search API to search file with different arguments as "extension", "name", "size" ...
//The design should be maintainable to add new contraints.
//

class Directory extends Entity{

    private List<Entity> children;

    public Directory(String name, int size) {
        super(name, size);
        children = new ArrayList<>();

    }

    public void addEntity(Entity entity) {
        this.children.add(entity);
    }

    public List<Entity> getChildren() {
        return children;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
}
