package ca.gbc.friendshipservice.controller;

import ca.gbc.friendshipservice.dto.FriendshipRequest;
import ca.gbc.friendshipservice.dto.FriendshipResponse;
import ca.gbc.friendshipservice.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friendship")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @PostMapping("/sendRequest")
    public void sendFriendRequest(@RequestBody FriendshipRequest friendRequest) {
        friendshipService.sendFriendRequest(friendRequest);
    }

    @GetMapping("/getFriends/{userId}")
    public FriendshipResponse getFriends(@PathVariable Long userId) {
        return friendshipService.getFriends(userId);
    }
}