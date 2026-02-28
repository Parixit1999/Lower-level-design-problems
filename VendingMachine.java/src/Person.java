import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

abstract class Person {
    private String id;
    private String name;
    private UserRole role;

    public Person(String id, String name, UserRole role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }
}
