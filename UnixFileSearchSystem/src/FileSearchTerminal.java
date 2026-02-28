//Design Unix File Search API to search file with different arguments as "extension", "name", "size" ...
//The design should be maintainable to add new contraints.
//

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
