package ca.gbc.friendshipservice.service;

import ca.gbc.friendshipservice.dto.FriendshipRequest;
import reactor.core.publisher.Mono;

public interface FriendshipService {

    Mono<String> sendRequest(FriendshipRequest friendshipRequest);
}
