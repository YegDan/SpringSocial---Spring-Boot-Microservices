package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService{
    private final WebClient client;


    private final CommentRepository commentRepository;
    //private final
    @Override
    public void createComment(CommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .postId(commentRequest.getPostId())
                .userId(commentRequest.getUserId())
                .text(commentRequest.getText())
                .build();
        commentRepository.save(comment);
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
                .text(comment.getText())
                .createdTime(currentDateTime)
                .build();
    }
    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
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
