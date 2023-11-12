package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

public class AppTest {

    Javalin app;
    private static MockWebServer mockServer;
    @BeforeEach
    public final void setUp() throws SQLException {
        app = App.getApp();
    }

    @BeforeAll
    public static void startServer() {
        mockServer = new MockWebServer();
    }

    @AfterAll
    public static void shutdownServer() throws IOException {
        mockServer.shutdown();
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
    public void testCreateUrlCheck() {
        JavalinTest.test(app, (server, client) -> {
            String file = "./src/test/resources/index.html";
            String body = Files.readString(Paths.get(file));
            mockServer.enqueue(new MockResponse().setBody(body));
            mockServer.enqueue(new MockResponse().setBody(body));
            mockServer.start();
            String baseUrl = mockServer.url("/").toString();
            var response = client.post("/urls", "url=" + baseUrl);
            assertThat(response.code()).isEqualTo(200);
            var response2 = client.get("/urls/1");
            assertThat(response2.code()).isEqualTo(200);
            var response3 = client.post("/urls/1/checks");
            assertThat(response3.code()).isEqualTo(200);
            var response4 = client.get("/urls/1");
            assertThat(response4.body().string()).contains("Хекслет — онлайн-школа программирования, онлайн-обучение ИТ-профессиям");
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
