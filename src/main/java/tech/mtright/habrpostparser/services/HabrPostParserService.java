package tech.mtright.habrpostparser.services;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tech.mtright.habrpostparser.model.Hub;
import tech.mtright.habrpostparser.model.Post;
import tech.mtright.habrpostparser.model.Tag;

import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static tech.mtright.habrpostparser.utils.ParserUtils.getDocumentFromLink;

@Log4j2
@Service
public class HabrPostParserService implements PostParserService {
    private static final String TITLE = "h1";
    private static final String AUTHOR_NAME = "user-info__nickname user-info__nickname_small";
    private static final String POST_BODY = "#post-content-body";
    private static final String ARTICLE_URL = "data-io-article-url";
    private static final String POST_TIME = "span.post__time";
    private static final String TIME_PUBLISHED = "data-time_published";
    private static final String COMPANY_ABOUT_BLOCK = "div.company-info__about";
    private static final String COMPANY_TITLE = "a.page-header__info-title";
    private static final String HUBS_LIST = "inline-list inline-list_fav-tags js-post-hubs";
    private static final String TAGS_LIST = "inline-list inline-list_fav-tags js-post-tags";
    private static final String VOTES_COUNTER = "voting-wjt voting-wjt_post js-post-vote";
    private static final String POST_VIEWS = "post-stats__views-count";
    private static final String MEGAPOST_META = "megapost-head__meta ";
    private static final String MEGAPOST_ARTICLE_URL = "data-io-article-url";
    private static final String MEGAPOST_HUBS = "megapost-head__hubs list list_inline";
    private static final String MEGAPOST = "article article_megapost";

    @SneakyThrows
    public Optional<Post> parsePost(int id) {
        String url = "https://habr.com/ru/post/" + id;
        Document doc;

        try {
            doc = getDocumentFromLink(url);
        } catch (ConnectException e) {
            try {
                doc = getDocumentFromLink(url);
            } catch (ConnectException ex) {
                try {
                    doc = getDocumentFromLink(url);
                } catch (ConnectException e1) {
                    log.warn("failed to parse " + id + " because " + e1.getMessage());
                    return Optional.empty();
                }
            }
        } catch (HttpStatusException e) {
            return Optional.empty();
        }
        return isMegapost(doc) ? getMegapost(doc, id) : getPost(id, doc);
    }

    private Optional<Post> getPost(int id, Document doc) throws ParseException {
        Element article = doc.selectFirst("article");
        String title = article.selectFirst(TITLE).text();
        String author = article.getElementsByClass(AUTHOR_NAME).text();
        String link = article.selectFirst(POST_BODY).attr(ARTICLE_URL);
        String publishedDate = article.selectFirst(POST_TIME).attr(TIME_PUBLISHED).substring(0, 16);
        int votes = getVotes(doc);
        int views = getViews(doc);
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(publishedDate);
        String company = getCompanyTitle(doc);

        Set<Tag> tags = getTags(article);
        Set<Hub> hubs = getHubs(article);


        return Optional.of(Post.builder().author(author).company(company).link(link).postId(id).title(title)
                .views(views).votes(votes).hubs(hubs).tags(tags).date(date).build());
    }

    private Optional<Post> getMegapost(Document doc, int id) throws ParseException {
        String author = "Megapost";
        String title = doc.selectFirst(TITLE).text();
        String link = doc.selectFirst(POST_BODY).attr(MEGAPOST_ARTICLE_URL);
        String publishedDate = doc.getElementsByClass(MEGAPOST_META).select("li[data-time_published]").attr(TIME_PUBLISHED);
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(publishedDate.substring(0, 16));
        int votes = getVotes(doc);
        int views = getViews(doc);
        Set<Tag> tags = getTags(doc);
        Set<Hub> hubs = getMegapostHubs(doc);

        return Optional.of(Post.builder().author(author).company(null).link(link).postId(id).title(title)
                .views(views).votes(votes).hubs(hubs).tags(tags).date(date).build());
    }

    private Set<Hub> getMegapostHubs(Document doc) {
        Set<Hub> hubs = new HashSet<>();
        Elements hubElements = doc.getElementsByClass(MEGAPOST_HUBS).select("li");
        for (Element hubElement : hubElements) {
            hubs.add(new Hub(hubElement.text()));
        }
        return hubs;
    }

    private boolean isMegapost(Document doc) {
        return doc.getElementsByClass(MEGAPOST).size() > 0;
    }

    private int getVotes(Document doc) {
        return Integer.parseInt(doc.getElementsByClass(VOTES_COUNTER).select("span").get(0).text().replace("–", "-"));
    }

    private int getViews(Document document) {
        String views = document.getElementsByClass(POST_VIEWS).text();
        if (views.contains("k")) {
            return (int) (Double.parseDouble(views.replace("k", "").replace(",", ".")) * 1000);
        } else {
            return Integer.parseInt(views);
        }
    }

    private Set<Hub> getHubs(Element article) {
        Set<Hub> hubs = new HashSet<>();
        Elements hubElements = article.getElementsByClass(HUBS_LIST).select("li");
        for (Element hubElement : hubElements) {
            hubs.add(new Hub(hubElement.text()));
        }
        return hubs;
    }

    private Set<Tag> getTags(Element article) {
        Set<Tag> tags = new HashSet<>();
        Elements tagElements = article.getElementsByClass(TAGS_LIST).select("li");
        for (Element tagElement : tagElements) {
            tags.add(new Tag(tagElement.text()));
        }
        return tags;
    }

    private String getCompanyTitle(Document document) {
        Element companyBlock = document.selectFirst(COMPANY_ABOUT_BLOCK);
        if (companyBlock != null) {
            return companyBlock.selectFirst(COMPANY_TITLE).text();
        } else {
            return null;
        }
    }
}
