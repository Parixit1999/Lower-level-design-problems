import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

class User implements Identity {
    private String name;
    private String  id;

    public User(String name) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String getUniqueId() {
        return this.id;
    }
}
