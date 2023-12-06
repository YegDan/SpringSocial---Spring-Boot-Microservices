package ca.gbc.commentservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import jakarta.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Document(value="Comment")
@Entity
@Table(name="comment")
public class Comment {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String postId;
    private Long userId;
    private String text;


}
