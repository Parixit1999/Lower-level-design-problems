//Design Unix File Search API to search file with different arguments as "extension", "name", "size" ...
//The design should be maintainable to add new contraints.
//

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
