//Design Unix File Search API to search file with different arguments as "extension", "name", "size" ...
//The design should be maintainable to add new contraints.
//

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
