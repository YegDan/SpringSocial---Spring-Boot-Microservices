package ca.gbc.friendshipservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
public class Friendship {

    @Id
    private Long id;
    private Long userId;
    @Getter
    private Long friendId;

}
