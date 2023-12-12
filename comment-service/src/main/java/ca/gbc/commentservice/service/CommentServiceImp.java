package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentRes;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.dto.PostRes;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImp implements CommentService{
    private final WebClient client;
    @Value("${post.service.url}")
    private String postApiUri;


    private final CommentRepository commentRepository;
    //private final
    @Override
    @Transactional
    public void createComment(CommentRequest commentRequest) {
        String postApiUriWithId = postApiUri + "/" + commentRequest.getPostId();
        client.get()
                .uri(postApiUriWithId)
                .retrieve()
                .bodyToMono(PostRes.class)
                .doOnSuccess(postRes -> {
                    if (postRes != null) {

                        Comment comment = Comment.builder()
                                .postId(commentRequest.getPostId())
                                .text(commentRequest.getText())
                                .userId(postRes.getUserId())
                                .username(postRes.getUsername())
                                .build();
                        commentRepository.save(comment);
                        log.info("Comment {} is saved", comment.getId());

                    }
                }).doOnError(error -> {
                    log.error("Error calling Post service", error);

                })
                .blockOptional();
    }

    public List<CommentRes> getCommentsByPostId(String postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> {
                    CommentRes commentRes = new CommentRes();
                    commentRes.setCommentId(comment.getId());
                    commentRes.setCaption(comment.getText());
                    return commentRes;
                })
                .collect(Collectors.toList());
    }






    @Override
    public List<CommentResponse> getAllComments() {

        List<Comment> allComments = commentRepository.findAllComments();

        return allComments.stream().map(this::mapToCommentResponse).toList();

    }

    public CommentResponse mapToCommentResponse(Comment comment){
        LocalDateTime currentDateTime = LocalDateTime.now();
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .username(comment.getUsername())
                .text(comment.getText())
                .createdTime(currentDateTime)
                .build();
    }
    @Override
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long updateComment(Long id, CommentRequest commentRequest) {
//        Optional<User> possibleUser = userRepository.findById(id);
//        if(possibleUser.isPresent()){
//            User user = possibleUser.get();
//            user.setFullName(userRequest.getFullName());
//            user.setUsername(userRequest.getUsername());
//            user.setPassword(userRequest.getPassword());
//            user.setEmail(userRequest.getEmail());
//            user.setBio(userRequest.getBio());
//            return userRepository.save(user).getId();
//        } else {
//
//            throw new RuntimeException("User not found with ID: " + id);
//        }
        Optional<Comment> possibleComment = commentRepository.findById(id);
        if(possibleComment.isPresent()){
            Comment comment = possibleComment.get();
            comment.setText(commentRequest.getText());

            return commentRepository.save(comment).getId();
        }else {

            throw new RuntimeException("Comment not found with ID: " + id);
        }


    }
}
