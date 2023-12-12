package ca.gbc.friendshipservice.service;

import ca.gbc.friendshipservice.dto.FriendshipResponse;
import ca.gbc.friendshipservice.model.Friendship;
import ca.gbc.friendshipservice.repository.FriendshipRepository;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipServiceImpl {

    private final FriendshipRepository friendshipRepository;

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<FriendshipResponse> getFriendshipStatusList(String username) {

        List<Friendship> friendshipList = friendshipRepository.findByUsername(username);
        List<FriendshipResponse> friendshipResponses = new ArrayList<>();

        if(!friendshipList.isEmpty()){
            for (Friendship friendship : friendshipList) {
                FriendshipResponse friendshipResponse = FriendshipResponse.builder()
                        .username(username)
                        .friendName(String.valueOf(friendship.getFriendName()))
                        .status(friendship.getStatus())
                        .build();

                friendshipResponses.add(friendshipResponse);
            }
        }
        return friendshipResponses;
    }
}