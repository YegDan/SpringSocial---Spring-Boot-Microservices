package ca.gbc.userservice.repository;
import ca.gbc.userservice.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);

//    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.id = :id")
//    boolean existsById(@Param("id") String username);
    boolean existsById(Long id);

    Optional<User> findById(Long id);



    void deleteById(Long id);

    @Query("SELECT u FROM User u")
    List<User> findAllUsers();




}
