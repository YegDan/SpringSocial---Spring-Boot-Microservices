package ca.gbc.postservice.service;

import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.dto.PostResponse;

import java.util.List;
import java.util.Optional;

public interface PostService {

    void createPost(PostRequest postRequest);
    String updatePost(String postId, PostRequest postRequest);
    void deletePost(String postId);
    List<PostResponse> getAllPosts();

}
