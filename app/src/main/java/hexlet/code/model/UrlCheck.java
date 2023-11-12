package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public final class UrlCheck {
    private Long id;
    private int statusCode;
    private Long urlId;

    @ToString.Include
    private String title;
    @ToString.Include
    private String h1;
    @ToString.Include
    private String description;

    private Timestamp createdAt;

    public UrlCheck(int statusCode, String title, String h1, String description, Timestamp createdAt) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.createdAt = createdAt;
    }
}
