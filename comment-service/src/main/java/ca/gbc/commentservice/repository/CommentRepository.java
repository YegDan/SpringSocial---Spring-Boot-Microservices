package ca.gbc.commentservice.repository;

import ca.gbc.commentservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
//import org.springframework.data.mongodb.repository.DeleteQuery;
//import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("SELECT u FROM Comment u")
    List<Comment> findAllComments();

    @Query("SELECT c FROM Comment c WHERE c.postId = :postId")
    List<Comment> findByPostId(@Param("postId") String postId);
}
