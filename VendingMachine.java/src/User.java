import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

class User extends Person {
    public User(String id, String name) {
        super(id, name, UserRole.USER_ROLE);
    }

}
