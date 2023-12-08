    package ca.gbc.commentservice.dto;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDateTime;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class CommentResponse {

        private Long id;
        private String postId;
        private Long userId;
        private String username;
        private String text;
    //    private String username;
        private LocalDateTime createdTime;

    }
