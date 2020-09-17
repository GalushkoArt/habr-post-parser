package tech.mtright.habrpostparser.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    int postId;
    Date date;
    String title;
    String link;
    String author;
    int votes;
    int views;
    @Nullable
    String company;
    Set<Tag> tags;
    Set<Hub> hubs;
}
