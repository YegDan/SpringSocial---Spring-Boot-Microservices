package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.model.Comment;
import ca.gbc.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService{


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

        List<Comment> allComments = commentRepository.findAll();

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
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }

    @Override
    public String updateComment(String id, CommentRequest commentRequest) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Comment comment = mongoTemplate.findOne(query, Comment.class);

        if(comment != null){
            comment.setText(commentRequest.getText());
            return commentRepository.save(comment).getId();
        }

        return id.toString();
    }
}
