package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UrlChecksController {
    public static void create(Context ctx) {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        try {
            var url = UrlRepository.find(urlId)
                    .orElseThrow(() -> new NotFoundResponse("Entity with id = " + urlId + "not found"));
            Document doc = Jsoup.connect(url.getName()).get();
            Elements newsHeadlines = doc.getAllElements();
            var response = Unirest.get(url.getName()).asString();
            var statusCode = response.getStatus();
            var createdAt = Timestamp.valueOf(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            String h1 = "", title = "", description = "";
            for (Element headline : newsHeadlines) {
                h1 = headline.selectFirst("h1") != null
                        ? headline.selectFirst("h1").text() : h1;
                title = headline.selectFirst("title") != null
                        ? headline.selectFirst("title").text() : title;
                description = headline.selectFirst("meta[name=description]") != null
                        ? headline.selectFirst("meta[name=description]").attr("content")
                        : description;
            }
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
