package ca.gbc.userservice.service;

import ca.gbc.userservice.dto.ToPostResponse;
import ca.gbc.userservice.dto.UserRequest;

import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.model.User;


import java.util.List;
import java.util.Optional;

public interface UserService {


    Long creatUser(UserRequest userRequest);
    void deleteUser(Long id);
    Long updateUser(Long id, UserRequest userRequest);
    List<UserResponse> getAllUsers();
    Optional<ToPostResponse> userExists(Long id);


}
