package tech.mtright.habrpostparser.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.mtright.habrpostparser.model.Post;
import tech.mtright.habrpostparser.services.BatchPostParserService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api")
public class BatchParserController {
    @Autowired
    private BatchPostParserService batchPostParserService;

    @GetMapping(value = "/getPosts", produces = "application/json;charset=UTF-8")
    public List<Post> getPostsBetween(@RequestParam int start, @RequestParam int end) {
        log.info("Processing batch between {} and {}", start, end);
        return batchPostParserService.getBatchOfPostsBetweenId(start, end);
    }

    @GetMapping(value = "/getPostsById", produces = "application/json;charset=UTF-8")
    public List<Post> getPostsByPostId(@RequestParam List<Integer> ids) {
        log.info("Processing batch of ids list with size {}", ids.size());
        return batchPostParserService.getBatchOfPostsByIds(ids);
    }
}
