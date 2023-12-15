package ca.gbc.userservice.service;

import ca.gbc.userservice.dto.UserRes;
import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.model.User;
import ca.gbc.userservice.repository.UserRepository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository ;


    @Override
    @Transactional
    public Long creatUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {

            throw new RuntimeException("Username Already Exists.");
        }

        User user = User.builder()
                .fullName(userRequest.getFullName())
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .bio(userRequest.getBio())
                .build();

        return userRepository.save(user).getId();




    }

    @Transactional
    public boolean createFriendship(Long senderId, Long recipientId) {
        Optional<User> sender = userRepository.findById(senderId);
        Optional<User> recipient = userRepository.findById(recipientId);

        if (sender.isPresent() && recipient.isPresent()) {
            User senderUser = sender.get();
            User recipientUser = recipient.get();

            senderUser.getFollowing().add(recipientUser);
            recipientUser.getFollowers().add(senderUser);

            userRepository.save(senderUser);
            userRepository.save(recipientUser);

            return true;
        } else {
            return false;
        }
    }



    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("Id not found");
        }
    }

    @Override
    @Transactional
    public Long updateUser(Long id, UserRequest userRequest){

        Optional<User> possibleUser = userRepository.findById(id);
        if(possibleUser.isPresent()){
            User user = possibleUser.get();
            user.setFullName(userRequest.getFullName());
            user.setUsername(userRequest.getUsername());
            user.setPassword(userRequest.getPassword());
            user.setEmail(userRequest.getEmail());
            user.setBio(userRequest.getBio());
            return userRepository.save(user).getId();
        } else {

            throw new RuntimeException("User not found with ID: " + id);
        }



    }

    @Override
    @Transactional
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAllUsers();


        return users.stream().map(this::mapTpUserResponse).toList();
    }

    @Override

    public Optional<UserRes> userExists(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserRes(user.getId(), user.getUsername()));
    }

    public UserResponse mapTpUserResponse(User user){
        List<String> followerUsernames = user.getFollowers().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
        List<String> followingUsernames = user.getFollowing().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .bio(user.getBio())
                .followers(followerUsernames)
                .following(followingUsernames)
                .build();
    }
}
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository ;


//    @Override
//    public void creatUser(UserRequest userRequest) {
//
//        User user = User.builder()
//                .fullName(userRequest.getFullName())
//                .username(userRequest.getUsername())
//                .password(userRequest.getPassword())
//                .email(userRequest.getEmail())
//                .bio(userRequest.getBio())
//                .build();
//        userRepository.save(user);
//
//    }
//
//    @Override
//
//    public void deleteUser(Long id) {
//
//        userRepository.deleteById(id);
//    }
//
//    @Override
//
//    public Long updateUser(Long id, UserRequest userRequest) {
////        Query query = new Query();
////        query.addCriteria(Criteria.where("id").is(id));
////        User user = mongoTemplate.findOne(query,User.class);
////
////        if(user != null){
////            user.setFullName(userRequest.getFullName());
////            user.setUsername(userRequest.getUsername());
////            user.setPassword(userRequest.getPassword());
////            user.setEmail(userRequest.getEmail());
////            user.setBio(userRequest.getBio());
////            return userRepository.save(user).getId();
////        }
////
////        return id.toString();
//        User user = userRepository.findById(id).orElse(null);
//        if (user != null) {
//            user.setFullName(userRequest.getFullName());
//            user.setUsername(userRequest.getUsername());
//            user.setPassword(userRequest.getPassword());
//            user.setEmail(userRequest.getEmail());
//            user.setBio(userRequest.getBio());
//            userRepository.save(user);
//            return user.getId();
//        }
//        return null;
//
//    }
//
//    @Override
//    public List<UserResponse> getAllUsers() {
//        List<User> allUsers = userRepository.findAll();
//
//        return allUsers.stream().map(this::mapToUserResponse).toList();
//    }
//
//    public UserResponse mapToUserResponse(User user){
//        return UserResponse.builder()
//                .id(user.getId())
//                .fullName(user.getFullName())
//                .username(user.getUsername())
//                .bio(user.getBio())
//                .build();
//    }
//}
