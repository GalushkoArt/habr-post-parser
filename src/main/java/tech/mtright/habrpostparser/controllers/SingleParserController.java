package tech.mtright.habrpostparser.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.mtright.habrpostparser.model.Post;
import tech.mtright.habrpostparser.services.PostParserService;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SingleParserController {
    @Autowired
    private PostParserService parserService;

    @GetMapping(value = "/getPost", produces = "application/json;charset=UTF-8")
    public Optional<Post> getPostByPostId(@RequestParam int id) {
        return parserService.parsePost(id);
    }
}
