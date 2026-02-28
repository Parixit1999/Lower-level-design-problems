//Design Unix File Search API to search file with different arguments as "extension", "name", "size" ...
//The design should be maintainable to add new contraints.
//

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
