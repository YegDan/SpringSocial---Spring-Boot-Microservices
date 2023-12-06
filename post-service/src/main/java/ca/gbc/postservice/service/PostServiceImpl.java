package ca.gbc.postservice.service;

import ca.gbc.postservice.dto.PostRequest;
import ca.gbc.postservice.dto.PostResponse;
import ca.gbc.postservice.dto.UserRes;
import ca.gbc.postservice.model.Post;
import ca.gbc.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostServiceImpl implements PostService{
    private final WebClient client;
    @Value("${user.service.url}")
    private String userApiUri;

    private final PostRepository postRepository;
    private final MongoTemplate mongoTemplate;
    @Override
    public void createPost(PostRequest postRequest) {

        String userApiUriWithId = userApiUri + "/" + postRequest.getUserId();

        client.get()
                .uri(userApiUriWithId)
                .retrieve()
                .bodyToMono(UserRes.class)
                .doOnSuccess(userRes -> {
                    if (userRes != null) {
                        Post post = Post.builder()
                                .userId(postRequest.getUserId())
                                .caption(postRequest.getCaption())
                                .build();
                        postRepository.save(post);
                        log.info("Post {} is saved", post.getId());
                    }
                })
                .doOnError(error -> {
                    log.error("Error calling User service", error);

                })
                .blockOptional();
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
                .build();
    }
}
