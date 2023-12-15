package ca.gbc.friendshipservice.service;

import ca.gbc.friendshipservice.dto.FriendshipRequest;
import ca.gbc.friendshipservice.model.Friendship;
import ca.gbc.friendshipservice.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j

public class FriendshipServiceImp implements FriendshipService{
    private final FriendshipRepository friendshipRepository;
    private final WebClient client;

    @Value("${user.service.url}")
    private String userApiUri;

    @Override
    public Mono<String> sendRequest(FriendshipRequest friendshipRequest) {
        String userApiUriwithIds =userApiUri+ "/validate-users/" + friendshipRequest.getSenderId() + "/" + friendshipRequest.getRecipientId();
        return client.get()
                .uri(userApiUriwithIds)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> Mono.error(new RuntimeException("One or both users not found")))
                .bodyToMono(String.class)
                .flatMap(response -> {
                    Friendship friendship = Friendship.builder()
                            .recipientId(friendshipRequest.getRecipientId())
                            .senderId(friendshipRequest.getSenderId())
                            .build();
                    friendshipRepository.save(friendship);
                    return Mono.just("Friendship created successfully");
                })
                .doOnError(error -> log.error("Error in creating friendship: {}", error.getMessage()));

    }
}
