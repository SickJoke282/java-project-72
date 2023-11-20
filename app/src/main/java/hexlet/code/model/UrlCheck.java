package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
public final class UrlCheck {
    @Setter
    private Long id;
    private final int statusCode;
    @Setter
    private Long urlId;

    private final String title;
    private final String h1;
    private final String description;

    private final Timestamp createdAt;

    public UrlCheck(int statusCode, String title, String h1, String description, Timestamp createdAt) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.createdAt = createdAt;
    }

    public UrlCheck(int statusCode, String title, String h1, String description, Timestamp createdAt, Long urlId) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
        this.createdAt = createdAt;
        this.urlId = urlId;
    }
}
