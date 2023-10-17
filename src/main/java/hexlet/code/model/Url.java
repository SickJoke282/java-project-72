package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public final class Url {
    private Long id;

    @ToString.Include
    private String name;

    private Timestamp created_at;

    public Url(String newName, Timestamp newCreated_at) {
        name = newName;
        created_at = newCreated_at;
    }
}
