package ca.gbc.userservice.service;

import ca.gbc.userservice.dto.UserRes;
import ca.gbc.userservice.dto.UserRequest;

import ca.gbc.userservice.dto.UserResponse;


import java.util.List;
import java.util.Optional;

public interface UserService {


    Long creatUser(UserRequest userRequest);
    void deleteUser(Long id);
    Long updateUser(Long id, UserRequest userRequest);
    List<UserResponse> getAllUsers();
    Optional<UserRes> userExists(Long id);


    String getUserProfile(String username);
}
