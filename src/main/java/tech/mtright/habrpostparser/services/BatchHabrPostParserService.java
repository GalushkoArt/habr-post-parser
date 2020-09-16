package tech.mtright.habrpostparser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.mtright.habrpostparser.model.Post;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BatchHabrPostParserService implements BatchPostParserService {
    @Autowired
    private PostParserService postParserService;

    @Override
    public List<Post> getBatchOfPostsBetweenId(int start, int end) {
        if (start > end) {
            start = start ^ end;
            end = start ^ end;
            start = start ^ end;
        }
        if (start != end) {
            start = start % 2 == 1 ? start + 1 : start;
            end = end % 2 == 1 ? end -1 : end;
        }
        return Stream.iterate(start, e -> e + 2).limit((end - start) / 2 + 1).parallel()
                .map(postParserService::parsePost)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> getBatchOfPostsByIds(List<Integer> ids) {
        return ids.parallelStream()
                .map(postParserService::parsePost)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
