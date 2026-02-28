import java.util.*;

//Design Unix File Search API to search file with different arguments as "extension", "name", "size" ...
//The design should be maintainable to add new contraints.
//

public class Main {
public static void main(String[] args) {

    Directory root = new Directory("root", 10);
    Directory one = new Directory("Parixit", 20);
    root.addEntity(one);
    root.addEntity(new File("Sanghani", 25, "txt"));
    one.addEntity(new File("Dineshbhai", 50, "py"));

    FileSearchTerminal fileSearchTerminal = new FileSearchTerminal();

    System.out.println(fileSearchTerminal.searchFiles(root, new SearchCriteria(Arrays.asList(new ExtensionSearchArgument("py")))));

}
}
