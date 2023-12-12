package ca.gbc.friendshipservice.service;

import ca.gbc.friendshipservice.dto.FriendshipRequest;
import ca.gbc.friendshipservice.dto.FriendshipResponse;
import ca.gbc.friendshipservice.model.Friendship;

import java.util.List;

public interface FriendshipService {

    List<FriendshipResponse> getFriendshipStatusList(String username);

}
