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

        private String id;
        private String postId;
        private String userId;
        private String text;
    //    private String username;
        private LocalDateTime createdTime;

    }
