package tech.mtright.habrpostparser.controllers;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tech.mtright.habrpostparser.model.Post;
import tech.mtright.habrpostparser.services.BatchPostParserService;
import tech.mtright.habrpostparser.services.PostParserService;

import javax.swing.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static tech.mtright.habrpostparser.controllers.ControllerMockConfiguration.makeGetRequest;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = ControllerMockConfiguration.class)
public class BatchParserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BatchPostParserService batchPostParserService;

    @Before
    public void init() {
        List<Post> testList1 = List.of(Post.builder().title("Some Title").author("Какой-то автор").votes(666).build(),
                Post.builder().title("Неожиданное название").author("Strange author").views(555).company("Some Company").build());
        List<Post> testList2 = List.of(Post.builder().title("Какое-то название").author("Some author").company("abc").build(),
                Post.builder().title("Simple name").author("Простой автор").views(777).votes(1111).build());
        Mockito.when(batchPostParserService.getBatchOfPostsByIds(anyList())).thenReturn(testList1);
        Mockito.when(batchPostParserService.getBatchOfPostsBetweenId(anyInt(), anyInt())).thenReturn(testList2);
    }

    @SneakyThrows
    @Test
    public void getPostsBetweenTest() {
        MvcResult result = makeGetRequest(mockMvc, "/api/getPosts?start=124&end=456");
        assertThat(result.getResponse().getContentAsString()).contains("Какое-то название").contains("Some author")
                .contains("abc").contains("Simple name").contains("Простой автор").contains("777").contains("1111");
    }

    @SneakyThrows
    @Test
    public void getPostsByPostIdTest() {
        MvcResult result = makeGetRequest(mockMvc, "/api/getPostsById?ids=1,138,85");
        assertThat(result.getResponse().getContentAsString()).contains("Some Title").contains("Какой-то автор").contains("555")
                .contains("666").contains("Неожиданное название").contains("Strange author").contains("Some Company");
    }
}