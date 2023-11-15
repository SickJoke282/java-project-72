package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class UrlsController {
    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        Map<Long, UrlCheck> urlChecks = UrlCheckRepository.findLatestChecks();
        var page = new UrlsPage(urls, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + "not found"));
        List<UrlCheck> urlsCheck = UrlCheckRepository.findByUrlId(id);
        var page = new UrlPage(url, urlsCheck);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
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
            var createdAt = new Timestamp(System.currentTimeMillis());
            var url = new Url(urlFromName.toString(), createdAt);
            if (!UrlRepository.findByName(url.getName()).equals(Optional.empty())) {
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
