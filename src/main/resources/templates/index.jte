@import hexlet.code.dto.BasePage
@param BasePage page

@template.layout.page(
content = @`
    <head>
        <title>Page Analyzer</title>
    </head>
    <h3>${page.getMessage()}</h3>
`
)