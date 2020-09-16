package tech.mtright.habrpostparser.controllers;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tech.mtright.habrpostparser.services.BatchPostParserService;
import tech.mtright.habrpostparser.services.PostParserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConfiguration
@ComponentScan("tech.mtright.habrpostparser.controllers")
public class ControllerMockConfiguration {
    @MockBean
    private PostParserService parserService;

    @MockBean
    private BatchPostParserService batchPostParserService;

    static MvcResult makeGetRequest(MockMvc mockMvc, String getRequest) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .get(getRequest)
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andReturn();
    }
}
