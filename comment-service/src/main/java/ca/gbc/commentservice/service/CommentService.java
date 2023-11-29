package ca.gbc.commentservice.service;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    void createComment(CommentRequest commentRequest);
    List<CommentResponse> getAllComments();
    void deleteComment(String id);
    String updateComment(String id, CommentRequest commentRequest);
}
