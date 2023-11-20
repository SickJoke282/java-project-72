package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
public final class Url {
    @Setter
    private Long id;

    private final String name;
    private final Timestamp createdAt;

    public Url(String newName, Timestamp newCreatedAt) {
        name = newName;
        createdAt = newCreatedAt;
    }
}
