package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
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
    public final void setUp() throws SQLException, IOException {
        app = App.getApp();
    }

    @BeforeAll
    public static void startServer() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
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
    public void testCreateUrlCheck() throws SQLException, IOException {
        String file = "./src/test/resources/index.html";
        String body = Files.readString(Paths.get(file));
        mockServer.enqueue(new MockResponse().setBody(body));
        String baseUrl = mockServer.url("/").toString();
        var actualUrl = new Url(baseUrl, new Timestamp(System.currentTimeMillis()));
        UrlRepository.save(actualUrl);
        JavalinTest.test(app, (server, client) -> {
            client.post("/urls/" + actualUrl.getId() + "/checks");
            var response = client.get("/urls/" + actualUrl.getId());
            assertThat(response.code()).isEqualTo(200);
        });
        var actualCheckUrl = UrlCheckRepository
                .findLatestChecks().get(actualUrl.getId());
        assertThat(actualCheckUrl).isNotNull();
        assertThat(actualCheckUrl.getTitle()).isEqualTo(
                "Хекслет — онлайн-школа программирования, онлайн-обучение ИТ-профессиям");
        assertThat(actualCheckUrl.getH1()).isEqualTo(
                "Лучшая школа программирования по версии пользователей Хабра");
        assertThat(actualCheckUrl.getDescription()).isEqualTo(
                "Хекслет — лучшая школа программирования по версии пользователей Хабра. "
                        + "Авторские программы обучения с практикой и готовыми проектами в резюме. "
                        + "Помощь в трудоустройстве после успешного окончания обучения");
    }

    @Test
    void testStore() throws SQLException {
        String inputUrl = "https://ru.hexlet.io";

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + inputUrl;
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
        });

        Url actualUrl = UrlRepository.findByName(inputUrl).orElse(null);

        assertThat(actualUrl).isNotNull();
        assertThat(actualUrl.getName()).isEqualTo(inputUrl);
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
