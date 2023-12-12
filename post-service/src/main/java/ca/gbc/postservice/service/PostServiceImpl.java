package ca.gbc.postservice.service;

import ca.gbc.postservice.dto.*;
import ca.gbc.postservice.model.Post;
import ca.gbc.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostServiceImpl implements PostService{
    private final WebClient client;
    @Value("${user.service.url}")
    private String userApiUri;
    @Value("${comment.service.url}")
    private String commentApiUri;
    

    private final PostRepository postRepository;
    private final MongoTemplate mongoTemplate;
    @Override
    public String createPost(PostRequest postRequest){

        String userApiUriWithId = userApiUri + "/" + postRequest.getUserId();

        UserRes userRes = client.get()
                .uri(userApiUriWithId)
                .retrieve()
                .bodyToMono(UserRes.class)
                .block();

        if (userRes != null) {
            Post post = Post.builder()
                    .userId(userRes.getUserId())
                    .caption(postRequest.getCaption())
                    .username(userRes.getUsername())
                    .build();
            postRepository.save(post);
            log.info("Post {} is saved", post.getId());
            return "Success: Post created successfully";
        } else {
            throw new RuntimeException("User not found");
        }


//        client.get()
//                .uri(userApiUriWithId)
//                .retrieve()
//                .bodyToMono(UserRes.class)
//                .doOnSuccess(userRes -> {
//                    if (userRes != null) {
//                        Post post = Post.builder()
//                                .userId(userRes.getUserId())
//                                .caption(postRequest.getCaption())
//                                .username(userRes.getUsername())
//                                .build();
//                        postRepository.save(post);
//                        log.info("Post {} is saved", post.getId());
//
//                    }
//                })
//                .doOnError(error -> {
//                    log.error("Error calling User service", error);
//
//
//                })
//                .blockOptional();

        }

    public Optional<PostRes> postExists(String id) {
        return postRepository.findById(id)
                .map(post -> new PostRes(post.getId(), post.getUserId(), post.getUsername()));
    }

    public Optional<PostResponse> postById(String id){
        return postRepository.findById(id)
                .map(post -> new PostResponse(post.getId(), post.getUserId(), post.getCaption(), post.getUsername()));
    }



    @Override
    public String updatePost(String postId, PostRequest postRequest) {
        log.info("Updating a post with id {}", postId);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(postId));
        Post post = mongoTemplate.findOne(query, Post.class);

        if (post != null) {
            post.setCaption(postRequest.getCaption());
            post.setUserId(postRequest.getUserId());

            log.info("Post {} is updated", post.getId());
            return postRepository.save(post).getId();
        }

        return postId.toString();
    }

    @Override
    public void deletePost(String postId) {

        log.info("Post {} is deleted", postId);
        postRepository.deleteById(postId);
    }
    public List<CommentRes> getSpecificPost(String id) {
        String commentApiUriWithId = commentApiUri + "/" + id;
        try {
            return client.get()
                    .uri(commentApiUriWithId)
                    .retrieve()
                    .bodyToFlux(CommentRes.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {

            throw new ResponseStatusException(e.getStatusCode(), "Error fetching comments: " + e.getMessage(), e);
        } catch (Exception e) {

            throw new RuntimeException("Error fetching comments", e);
        }
    }


    @Override
    public List<PostResponse> getAllPosts() {


        log.info("Returning a list of posts");
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapToPostResponse).toList();

    }
    

    private PostResponse mapToPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .caption(post.getCaption())
                .userId(post.getUserId())
                .username(post.getUsername())
                .build();
    }
}
