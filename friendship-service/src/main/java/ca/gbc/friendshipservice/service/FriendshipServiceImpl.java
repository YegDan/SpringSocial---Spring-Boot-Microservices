//package ca.gbc.friendshipservice.service;
//
//import ca.gbc.friendshipservice.dto.FriendshipResponse;
//import ca.gbc.friendshipservice.model.Friendship;
//import ca.gbc.friendshipservice.repository.FriendshipRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class FriendshipServiceImpl implements FriendshipService {
//
//    private final FriendshipRepository friendshipRepository;
//    private final WebClient client;
//
//
//    @Override
//    public List<Friendship> getAllFriendships() {
//        // Return all friendships
//        return friendshipRepository.findAll();
//    }
//
//    @Override
//    public List<FriendshipResponse> getAllFriends() {
//
//
//        log.info("Returning a list of friends");
//        List<Friendship> friends = friendshipRepository.findAll();
//        return friends.stream().map(this::mapToFriendshipResponse).toList();
//
//    }
//
//    private FriendshipResponse mapToFriendshipResponse(Friendship friends) {
//        return FriendshipResponse.builder()
//                .FriendId(friendship.getId())
//                .caption(post.getCaption())
//                .friendshipId(post.getUserId())
//                .username(post.getUsername())
//                .build();
//    }
package ca.gbc.friendshipservice.service;

import ca.gbc.friendshipservice.dto.FriendshipRequest;
import ca.gbc.friendshipservice.dto.FriendshipResponse;
import ca.gbc.friendshipservice.model.Friendship;
import ca.gbc.friendshipservice.repository.FriendshipRepository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.conn.ConnectionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private WebClient webClient;
    @Value("${user.service.url}")
    private String userApiUri;

    @Override
    public void sendFriendRequest(FriendshipRequest friendRequest) {

        // Check if the users exist and get additional details
        FriendshipResponse user = getUserDetails(Collections.singletonList(friendRequest.getUserId()));
        FriendshipResponse friend = getUserDetails(Collections.singletonList(friendRequest.getFriendId()));

        // Create a friendship record
        Friendship friendship = new Friendship();
        friendship.setUserId(user.getFriendIds().get(0));
        friendship.setFriendId(friend.getFriendIds().get(0));

        friendshipRepository.save(friendship);
    }


    @Override
    public FriendshipResponse getFriends(Long userId) {
        // Retrieve friend ids from FriendshipRepository
        List<Friendship> friendships = friendshipRepository.findByUserId(userId);

        // Convert friend ids to FriendshipResponse
        List<Long> friendIds = friendships.stream()
                .map(Friendship::getFriendId)
                .collect(Collectors.toList());

        // Use WebClient to get user details from user-service
        FriendshipResponse response = getUserDetails(friendIds);

        return response;
    }

    private FriendshipResponse getUserDetails(List<Long> userIds) {
        return webClient.get()
                .uri("/user/getUserDetails?userIds={userIds}", userIds)
                .retrieve()
                .bodyToMono(FriendshipResponse.class)
                .block();
    }
}