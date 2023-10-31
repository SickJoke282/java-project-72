package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

public class AppTest {

    Javalin app;

    @BeforeEach
    public final void setUp() throws SQLException {
        app = App.getApp();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=http://example.com";
            var response = client.post("/urls", requestBody);
            var createdAt = Timestamp.valueOf(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            Url url = new Url(requestBody, createdAt);
            UrlRepository.save(url);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("http://example.com");
            var response2 = client.get("/urls/1");
            assertThat(response2.code()).isEqualTo(200);
            assertThat(response2.body().string()).contains("http://example.com");
        });
    }

    @Test
    public void testExistingUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=http://example.com";
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).containsOnlyOnce("http://example.com");
        });
    }

    @Test
    public void testBadUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=example.com";
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/99");
            assertThat(response.code()).isEqualTo(404);
        });
    }
}
