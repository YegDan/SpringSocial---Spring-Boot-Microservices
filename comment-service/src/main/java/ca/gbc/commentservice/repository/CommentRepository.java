package ca.gbc.commentservice.repository;

import ca.gbc.commentservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.mongodb.repository.DeleteQuery;
//import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
//    @DeleteQuery
//    void deleteById(String id);
}
