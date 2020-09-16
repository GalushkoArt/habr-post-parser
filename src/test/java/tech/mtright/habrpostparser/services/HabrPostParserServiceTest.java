package tech.mtright.habrpostparser.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import tech.mtright.habrpostparser.model.Hub;
import tech.mtright.habrpostparser.model.Post;
import tech.mtright.habrpostparser.model.Tag;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServiceMockConfiguration.class)
public class HabrPostParserServiceTest {
    @Autowired
    private PostParserService parserService;

    @Test
    public void parsePostWithCompanyTest() {
        Set<Hub> hubs = Set.of(new Hub("Блог компании Яндекс"), new Hub("Open source"));
        Set<Tag> tags = Set.of(new Tag("open source"), new Tag("яндекс"), new Tag("nginx"));
        Post post = parserService.parsePost(480090).orElseThrow();
        assertThat(post.getPostId()).isEqualTo(480090);
        assertThat(post.getTitle()).isEqualTo("Open source – наше всё");
        assertThat(post.getDate()).hasYear(2019).hasMonth(12).hasDayOfMonth(13);
        assertThat(post.getAuthor()).isEqualTo("bobuk");
        assertThat(post.getLink()).isEqualTo("https://habr.com/ru/company/yandex/blog/480090/");
        assertThat(post.getCompany()).isEqualTo("Яндекс");
        assertThat(post.getViews()).isGreaterThan(10000);
        assertThat(post.getVotes()).isGreaterThan(0);
        assertThat(post.getHubs()).isEqualTo(hubs);
        assertThat(post.getTags()).isEqualTo(tags);
    }

    @Test
    public void parsePostWithoutCompanyTest() {
        Set<Hub> hubs = Set.of(new Hub("Научно-популярное"),
                                new Hub("Визуализация данных"),
                                new Hub("Биотехнологии"),
                                new Hub("Открытые данные"),
                                new Hub("Здоровье"));
        Set<Tag> tags = Set.of(new Tag("covid-19"),
                                new Tag("коронавирус"),
                                new Tag("coronavirus"),
                                new Tag("эпидемия"),
                                new Tag("пандемия"));
        Post post = parserService.parsePost(491974).orElseThrow();
        assertThat(post.getPostId()).isEqualTo(491974);
        assertThat(post.getTitle()).isEqualTo("Коронавирус: почему надо действовать прямо сейчас");
        assertThat(post.getDate()).hasYear(2020).hasMonth(3).hasDayOfMonth(12);
        assertThat(post.getAuthor()).isEqualTo("five");
        assertThat(post.getLink()).isEqualTo("https://habr.com/ru/post/491974/");
        assertThat(post.getCompany()).isNull();
        assertThat(post.getViews()).isGreaterThan(100000);
        assertThat(post.getVotes()).isGreaterThan(0);
        assertThat(post.getHubs()).isEqualTo(hubs);
        assertThat(post.getTags()).isEqualTo(tags);
    }

    @Test
    public void parseMegapost() {
        Set<Hub> hubs = Set.of(new Hub("Удалённая работа"),
                new Hub("Управление персоналом"),
                new Hub("Офисы IT-компаний"),
                new Hub("Здоровье"));
        Set<Tag> tags = Set.of(new Tag("удаленка"),
                new Tag("удаленная работа"),
                new Tag("мотивация"),
                new Tag("управление персоналом"),
                new Tag("не фриланс"),
                new Tag("карьера"),
                new Tag("распределенные коллективы"),
                new Tag("изоляция"),
                new Tag("продуктивность"));
        Post post = parserService.parsePost(503078).orElseThrow();
        assertThat(post.getPostId()).isEqualTo(503078);
        assertThat(post.getTitle()).isEqualTo("Соцпакет при переходе на удалёнку: что и как мы сделали");
        assertThat(post.getDate()).hasYear(2020).hasMonth(5).hasDayOfMonth(21);
        assertThat(post.getAuthor()).isEqualTo("Megapost");
        assertThat(post.getLink()).isEqualTo("https://habr.com/ru/article/503078/");
        assertThat(post.getViews()).isGreaterThan(10000);
        assertThat(post.getVotes()).isGreaterThan(0);
        assertThat(post.getHubs()).isEqualTo(hubs);
        assertThat(post.getTags()).isEqualTo(tags);
    }

    @Test
    public void parsePostWithWrongPostId() {
        Optional<Post> post = parserService.parsePost(-99);
        assertThat(post.isEmpty()).isTrue();
    }

}