//Design Unix File Search API to search file with different arguments as "extension", "name", "size" ...
//The design should be maintainable to add new contraints.
//

abstract class Entity{
    private String name;
    private int size;

    public Entity(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public abstract boolean isDirectory();
}

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


interface SearchArgument{
    boolean isMatch(File file);
}

class ExtensionSearchArgument implements SearchArgument{

    String extension;

    public ExtensionSearchArgument(String extension){
        this.extension = extension;
    }

    @Override
    public boolean isMatch(File file) {
        return file.extension.equals(this.extension);
    }
}

class NameSearchArgument implements SearchArgument{

    String name;

    public NameSearchArgument(String name){
        this.name = name;
    }

    @Override
    public boolean isMatch(File file) {
        return file.getName().equals(this.name);
    }
}

class SizeSearchArgument implements SearchArgument {
    int size;

    public SizeSearchArgument(int size){
        this.size = size;
    }

    @Override
    public boolean isMatch(File file) {
        return file.getSize() == this.size;
    }
}

class SearchCriteria{
    List<SearchArgument> searchArguments;

    public SearchCriteria(List<SearchArgument> searchArguments) {
        this.searchArguments = searchArguments;
    }

    public boolean isMatch(File file) {
        boolean matched = true;

        for(SearchArgument searchArgument: searchArguments) {
            matched = matched & searchArgument.isMatch(file);
        }

        return matched;
    }
}

interface SearchOperation{
    List<File> searchFiles(Entity entity, SearchCriteria searchCriteria);
}

class FileSearchTerminal implements  SearchOperation{

    @Override
    public List<File> searchFiles(Entity entity, SearchCriteria searchCriteria) {

        List<File> fileList = new ArrayList<>();
        Stack<Entity> stack = new Stack<>();

        if(!entity.isDirectory()) {
            File file = (File) entity;
            if(searchCriteria.isMatch(file)) {
                fileList.add(file);
            }
            return fileList;
        }

        stack.add(entity);

        while(!stack.isEmpty()) {
            Entity topEntity = stack.pop();
            if(topEntity.isDirectory()) {
                Directory topDirectory = (Directory) topEntity;
                for(Entity children: topDirectory.getChildren()) {
                    stack.add(children);
                }
            } else {
                File topFile = (File) topEntity;
                if(searchCriteria.isMatch(topFile)) {
                    fileList.add(topFile);
                }
            }
        }

        return fileList;
    }
}


void main() {

    Directory root = new Directory("root", 10);
    Directory one = new Directory("Parixit", 20);
    root.addEntity(one);
    root.addEntity(new File("Sanghani", 25, "txt"));
    one.addEntity(new File("Dineshbhai", 50, "py"));

    FileSearchTerminal fileSearchTerminal = new FileSearchTerminal();

    System.out.println(fileSearchTerminal.searchFiles(root, new SearchCriteria(Arrays.asList(new ExtensionSearchArgument("py")))));


}
