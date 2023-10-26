package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UrlsController {
    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + "not found"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    }

    public static void build(Context ctx) {
        var page = new BasePage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        try {
            String name = ctx.formParamAsClass("url", String.class).get();
            URL urlFromName = new URL(name.trim().toLowerCase());
            Pattern pattern = Pattern.compile("(https://|http://|ftp://)((\\w|\\.|)+(:[\\d]+)?[^/])");
            Matcher matcher = pattern.matcher(urlFromName.toString());
            matcher.find();
            var createdAt = Timestamp.valueOf(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            var url = new Url(matcher.group(), createdAt);
            if (!UrlRepository.getEntities().stream()
                    .filter(expectedUrl -> expectedUrl.getName().equals(url.getName()))
                    .collect(Collectors.toSet())
                    .equals(new LinkedHashSet<>())) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.redirect(NamedRoutes.urlsPath());
            } else {
                UrlRepository.save(url);
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.redirect(NamedRoutes.urlsPath());
            }
        } catch (MalformedURLException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect(NamedRoutes.mainPath());
        }
    }
}
