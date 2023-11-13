@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
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
                        <td>${page.getUrl().getCreatedAt().toString()}</td>
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
                        @for(int i = page.getUrlCheck().size() - 1; i >= 0; i--)
                            <tr>
                                <td>
                                    ${page.getUrlCheck().get(i).getId()}
                                </td>
                                <td>
                                    ${page.getUrlCheck().get(i).getStatusCode()}
                                </td>
                                <td>
                                    ${page.getUrlCheck().get(i).getTitle()}
                                </td>
                                <td>
                                    ${page.getUrlCheck().get(i).getH1()}
                                </td>
                                <td>
                                    ${page.getUrlCheck().get(i).getDescription()}
                                </td>
                                <td>
                                    ${page.getUrlCheck().get(i).getCreatedAt().toString()}
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