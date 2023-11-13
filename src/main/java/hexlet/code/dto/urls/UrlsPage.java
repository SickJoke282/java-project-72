package hexlet.code.dto.urls;
import java.util.List;

import hexlet.code.model.Url;
import hexlet.code.dto.BasePage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UrlsPage extends BasePage {
    private List<Url> urls;
}
