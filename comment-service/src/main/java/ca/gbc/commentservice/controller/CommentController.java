package ca.gbc.commentservice.controller;

import ca.gbc.commentservice.dto.CommentRequest;
import ca.gbc.commentservice.dto.CommentResponse;
import ca.gbc.commentservice.service.CommentServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentServiceImp commentServiceImp;
    @PostMapping
    public ResponseEntity<?> creatComment(@RequestBody CommentRequest commentRequest){

        commentServiceImp.createComment(commentRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponse> getAllComments(){

        return commentServiceImp.getAllComments();
    }
    @PutMapping({"{commentId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateComment(@PathVariable("commentId") String id
            ,@RequestBody CommentRequest commentRequest){
        String updatedCommentId = commentServiceImp.updateComment(id, commentRequest);
        HttpHeaders header = new HttpHeaders();
        header.add("Location", "/api/comments/" + updatedCommentId);

    }

    @DeleteMapping({"{commentId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("commentId") String id){
        commentServiceImp.deleteComment(id);
    }

}
