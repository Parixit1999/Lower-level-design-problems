//Design Unix File Search API to search file with different arguments as "extension", "name", "size" ...
//The design should be maintainable to add new contraints.
//

class File extends Entity {
    String extension;

    public File(String name, int size, String extension) {
        super(name, size);
        this.extension = extension;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }
}
