package ca.gbc.postservice.controller;

import ca.gbc.postservice.dto.*;
import ca.gbc.postservice.service.PostServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/post")
public class PostController {

    private final PostServiceImpl postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="user", fallbackMethod = "createPostFallback")
    @TimeLimiter(name="user")
    @Retry(name="user")
    public CompletableFuture<String> createPost(@RequestBody PostRequest postRequest) {

        postService.createPost(postRequest);
        return CompletableFuture.supplyAsync(() -> postService.createPost(postRequest));
    }

    public CompletableFuture<String> createPostFallback(PostRequest postRequest, RuntimeException e){
        log.error("Exception is: {}", e.getMessage());
        return CompletableFuture.completedFuture("FALLBACK INVOKED: failed to creat post. Please try again later.");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponse> getAllPosts() {

        return postService.getAllPosts();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentRes>> getCommentsForPost(@PathVariable String id) {
        try {
            List<CommentRes> comments = postService.getSpecificPost(id);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<PostRes> getPostbyId(@PathVariable String id) {
        return postService.postExists(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping({"/{postId}"})
    public ResponseEntity<?> updatePost(@PathVariable("postId") String postId, @RequestBody PostRequest postRequest) {
        String updatePostId = postService.updatePost(postId, postRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "/api/post/" + updatePostId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") String postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
