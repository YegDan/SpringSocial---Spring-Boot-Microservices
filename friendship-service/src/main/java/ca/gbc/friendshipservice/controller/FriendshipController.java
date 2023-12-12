package ca.gbc.friendshipservice.controller;

import ca.gbc.friendshipservice.dto.FriendshipResponse;
import ca.gbc.friendshipservice.service.FriendshipServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendship")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipServiceImpl friendshipService;

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<FriendshipResponse> getFriendshipStatusList(@PathVariable("username") String username){
        return friendshipService.getFriendshipStatusList(username);
    }

}