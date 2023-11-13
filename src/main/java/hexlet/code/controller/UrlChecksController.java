package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.Timestamp;

@Slf4j
public class UrlChecksController {
    public static void create(Context ctx) {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        try {
            var url = UrlRepository.find(urlId)
                    .orElseThrow(() -> new NotFoundResponse("Entity with id = " + urlId + "not found"));
            var response = Unirest.get(url.getName()).asString();
            Document doc = Jsoup.parse(response.getBody());
            int statusCode = response.getStatus();
            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            String title = doc.title();
            String h1 = doc.selectFirst("h1") != null
                    ? doc.selectFirst("h1").text() : "";
            String description = doc.selectFirst("meta[name=description]") != null
                    ? doc.selectFirst("meta[name=description]").attr("content")
                    : "";
            log.info("h1 = " + h1);
            log.info("description = " + description);
            var urlCheck = new UrlCheck(statusCode, title, h1, description, createdAt);
            UrlCheckRepository.save(urlCheck, url);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
        } finally {
            ctx.redirect(NamedRoutes.urlPath(urlId));
        }
    }
}
