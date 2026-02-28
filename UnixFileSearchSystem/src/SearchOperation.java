//Design Unix File Search API to search file with different arguments as "extension", "name", "size" ...
//The design should be maintainable to add new contraints.
//

interface SearchOperation{
    List<File> searchFiles(Entity entity, SearchCriteria searchCriteria);
}
