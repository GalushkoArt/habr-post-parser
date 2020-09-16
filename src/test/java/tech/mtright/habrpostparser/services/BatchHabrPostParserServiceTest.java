package tech.mtright.habrpostparser.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import tech.mtright.habrpostparser.model.Post;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServiceMockConfiguration.class)
public class BatchHabrPostParserServiceTest {
    @Autowired
    private BatchPostParserService batchPostParserService;

    @Test
    public void getBatchOfPostsBetweenIdTest() {
        List<Post> batch = batchPostParserService.getBatchOfPostsBetweenId(500000, 500500);
        System.out.println(batch.size());
        assertThat(batch).size().isGreaterThan(40);
    }

    @Test
    public void getBatchOfPostsByIdsTest() {
        List<Integer> ids = List.of(500000, 500002, 500004, 500006, 500008, 500010, 500012, 500014, 500016, 500018,
                500020, 500022, 500024, 500026, 500028, 500030, 500032, 500034, 500036, 500038);
        List<Post> batch = batchPostParserService.getBatchOfPostsByIds(ids);
        assertThat(batch).size().isGreaterThan(10);
    }
}