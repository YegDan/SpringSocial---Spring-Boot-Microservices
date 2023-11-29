package ca.gbc.postservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(value = "Post")
public class Post {
    @Id
    private String id;
    private Long userId;
    private String caption;
//    private int likes;
//    private List<String> commentId;
}
