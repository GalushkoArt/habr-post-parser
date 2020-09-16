package tech.mtright.habrpostparser.services;

import tech.mtright.habrpostparser.model.Post;

import java.util.Optional;

public interface PostParserService {
    Optional<Post> parsePost(int id);
}
