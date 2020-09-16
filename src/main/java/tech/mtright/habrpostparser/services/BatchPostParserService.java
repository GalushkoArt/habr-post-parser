package tech.mtright.habrpostparser.services;

import tech.mtright.habrpostparser.model.Post;

import java.util.List;

public interface BatchPostParserService {
    List<Post> getBatchOfPostsBetweenId(int start, int end);

    List<Post> getBatchOfPostsByIds(List<Integer> ids);
}
