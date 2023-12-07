package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    void createComment(CommentRequest commentRequest);
    List<CommentResponse> getAllComments();
    void deleteComment(Long id);
    Long updateComment(Long id, CommentRequest commentRequest);
}
