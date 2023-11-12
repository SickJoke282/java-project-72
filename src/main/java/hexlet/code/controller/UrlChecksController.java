package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlChecksController {
    public static void create(Context ctx) {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        try {
            var url = UrlRepository.find(urlId)
                    .orElseThrow(() -> new NotFoundResponse("Entity with id = " + urlId + "not found"));
            var response = Unirest.get(url.getName()).asString();
            var statusCode = response.getStatus();
            var createdAt = Timestamp.valueOf(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            var title = response.getBody().substring(
                    response.getBody().indexOf("<title"), response.getBody().indexOf("</title>"));
            title = title.substring(title.indexOf(">") + 1);
            var h1 = response.getBody().substring(
                    response.getBody().indexOf("<h1"), response.getBody().indexOf("</h1>"));
            h1 = h1.substring(h1.indexOf(">") + 1);
            Pattern pattern = Pattern.compile(
                    "<meta([a-z\\s=\":]*)description([a-z\\s=\":]*)content=\"[^\\n]*$");
            response = Unirest.get(url.getName()).asString();
            String str = response.getBody().substring(0, response.getBody().indexOf("<script"));
            Matcher matcher = pattern.matcher(str);
            System.out.println(str);
            System.out.println(matcher.find());
            var description = matcher.find()
                    ? matcher.group()
                    .substring(matcher.group()
                            .indexOf("content=\"") + 1, matcher.group().indexOf("\">"))
                    : "";
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
