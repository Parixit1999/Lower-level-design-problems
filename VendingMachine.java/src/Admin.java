import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

class Admin extends Person {
    public Admin(String id, String name) {
        super(id, name, UserRole.ADMIN_ROLE);
    }
}
