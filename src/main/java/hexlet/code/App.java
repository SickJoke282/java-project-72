package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.dto.BasePage;
import hexlet.code.repository.BaseRepository;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
public class App {
    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }
    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }
    public static Javalin getApp() throws IOException, SQLException {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");

        var dataSource = new HikariDataSource(hikariConfig);
        var url = App.class.getClassLoader().getResource("schema.sql");
        System.out.println(url.getFile());
        var file = new File(url.getFile());
        System.out.println(file.toPath());
        var sql = Files.lines(file.toPath())
                .collect(Collectors.joining("\n"));
        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;
        Javalin app = Javalin.create(config -> config.plugins.enableDevLogging());
        JavalinJte.init(createTemplateEngine());

        app.get("/", ctx -> {
            var page = new BasePage("Hello, World!");
            ctx.render("index.jte", Collections.singletonMap("page", page));
        });
        return app;
    }
    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();
        app.start(getPort());
    }
}
