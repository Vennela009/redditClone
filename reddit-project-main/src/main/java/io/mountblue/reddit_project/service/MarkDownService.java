package io.mountblue.reddit_project.service;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.springframework.stereotype.Service;

 @Service
public class MarkDownService {
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    public String convertMarkdownToHtml(String markdown) {
        return renderer.render(parser.parse(markdown));
    }
}
