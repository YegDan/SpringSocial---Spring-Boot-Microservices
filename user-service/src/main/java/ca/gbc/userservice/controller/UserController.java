package ca.gbc.userservice.controller;

import ca.gbc.userservice.dto.UserRes;
import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequestMapping("/api/myAccount")
@RequiredArgsConstructor

public class UserController {
    private final UserServiceImpl userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserRequest userRequest){
        userService.creatUser(userRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserRes> getUserById(@PathVariable Long id) {
        return userService.userExists(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping({"/{userId}"})
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long id
            , @RequestBody UserRequest userRequest){
        Long updatedUserId = userService.updateUser(id, userRequest);
        HttpHeaders header = new HttpHeaders();
        header.add("Location", "/api/user/" + updatedUserId);
        return new ResponseEntity<>(header, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping({"/{userId}"})
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/validate-users/{senderId}/{recipientId}")
    public Mono<ResponseEntity<String>> validateUsers(@PathVariable Long senderId, @PathVariable Long recipientId) {
        boolean isValid = userService.createFriendship(senderId, recipientId);

        if (isValid) {
            return Mono.just(ResponseEntity.status(HttpStatus.CREATED).body("Both users are valid"));
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("One or both users not found"));
        }
    }

}
