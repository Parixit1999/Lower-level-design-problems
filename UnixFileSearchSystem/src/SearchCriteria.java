import java.util.*;

//Design Unix File Search API to search file with different arguments as "extension", "name", "size" ...
//The design should be maintainable to add new contraints.
//

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
