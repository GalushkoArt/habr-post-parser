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
import tech.mtright.habrpostparser.services.PostParserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static tech.mtright.habrpostparser.controllers.ControllerMockConfiguration.makeGetRequest;

@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = ControllerMockConfiguration.class)
public class SingleParserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostParserService parserService;

    @Before
    public void init() {
        Mockito.when(parserService.parsePost(anyInt())).thenReturn(Optional.of(Post.builder().company("Какая-то Компания")
                .votes(-6480).author("Any author").title("Какое-то название").views(489489).build()));
    }

    @SneakyThrows
    @Test
    public void getPostByPostIdTest() {
        MvcResult result = makeGetRequest(mockMvc, "/api/getPost?id=48");
        assertThat(result.getResponse().getContentAsString()).contains("Какая-то Компания").contains("Any author")
                .contains("489489").contains("Какое-то название").contains("-6480");
    }
}