package tech.mtright.habrpostparser.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ParserUtils {
    private ParserUtils() {}

    public static Document getDocumentFromLink(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla")
                .timeout(15000)
                .referrer("http://google.com")
                .get();
    }
}
