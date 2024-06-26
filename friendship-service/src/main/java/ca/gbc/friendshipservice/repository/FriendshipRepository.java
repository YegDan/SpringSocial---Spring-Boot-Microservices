package ca.gbc.friendshipservice.repository;

import ca.gbc.friendshipservice.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

}
