@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
@import java.time.format.DateTimeFormatter
@param UrlPage page

@template.layout.page(
    page = page,
    content = @`
    @if(page != null && page.getFlash() != null && page.getFlash().equals("Страница успешно проверена"))
        <div class="rounded-0 m-0 alert alert-dismissible fade show alert-success" role="alert">
            <p class="m-0">${page.getFlash()}</p>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    @elseif(page != null && page.getFlash() != null && page.getFlash().equals("Некорректный адрес"))
        <div class="rounded-0 m-0 alert alert-dismissible fade show alert-danger" role="alert">
            <p class="m-0">${page.getFlash()}</p>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    @endif
    <main class="flex-grow-1">
        <section>
            <div class="container-lg mt-5">
                <h1>Сайт: ${page.getUrl().getName()}</h1>

                <table class="table table-bordered table-hover mt-3">
                    <tbody>
                    <tr>
                        <td>ID</td>
                        <td>${page.getUrl().getId()}</td>
                    </tr>
                    <tr>
                        <td>Имя</td>
                        <td>${page.getUrl().getName()}</td>
                    </tr>
                    <tr>
                        <td>Дата создания</td>
                        <td>${page.getUrl().getCreatedAt()
                                                        .toLocalDateTime()
                                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                                        .toString()}</td>
                    </tr>
                    </tbody>
                </table>
                <h2 class="mt-5">Проверки</h2>
                <form method="post" action="${NamedRoutes.urlCheckPath(page.getUrl().getId())}">
                    <button type="submit" class="btn btn-primary">Запустить проверку</button>
                </form>
                <table class="table table-bordered table-hover mt-3">
                    <thead>
                    <tr>
                    <th class="col-1">ID</th>
                    <th class="col-1">Код ответа</th>
                    <th>title</th>
                    <th>h1</th>
                    <th>description</th>
                    <th class="col-2">Дата проверки</th>
                    </tr>
                    </thead>
                    <tbody>
                    @if(page.getUrlCheck() != null)
                        @for(var urlCheck : page.getUrlCheck())
                            <tr>
                                <td>
                                    ${urlCheck.getId()}
                                </td>
                                <td>
                                    ${urlCheck.getStatusCode()}
                                </td>
                                <td>
                                    ${urlCheck.getTitle()}
                                </td>
                                <td>
                                    ${urlCheck.getH1()}
                                </td>
                                <td>
                                    ${urlCheck.getDescription()}
                                </td>
                                <td>
                                    ${urlCheck.getCreatedAt()
                                                        .toLocalDateTime()
                                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                                        .toString()}
                                </td>
                            </tr>
                        @endfor
                    @endif
                    </tbody>
                </table>
            </div>
        </section>
    </main>
    `
)