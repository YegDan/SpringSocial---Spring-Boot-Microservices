package ca.gbc.postservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecificPostResponse {

    private String id;
    private Long userId;
    private String caption;
    private List<CommentRes> comments;
}
