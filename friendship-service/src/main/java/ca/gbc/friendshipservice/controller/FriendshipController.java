package ca.gbc.friendshipservice.controller;


import ca.gbc.friendshipservice.dto.FriendshipRequest;
import ca.gbc.friendshipservice.service.FriendshipServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequestMapping("/api/friendship")
@RequiredArgsConstructor

public class FriendshipController {
    private final FriendshipServiceImp friendshipServiceImp;


    @PostMapping("/send-request")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<String> sendRequest(@RequestBody FriendshipRequest friendshipRequest){
        return friendshipServiceImp.sendRequest(friendshipRequest);



    }

//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<UserResponse> getAllUsers(){
//        return userService.getAllUsers();
//    }



}
