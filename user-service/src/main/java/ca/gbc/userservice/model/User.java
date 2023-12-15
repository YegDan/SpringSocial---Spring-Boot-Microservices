package ca.gbc.userservice.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "__user")
public class User {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String bio;

    @ManyToMany
    @JoinTable(
            name = "user_followers", // The join table for the relationship
            joinColumns = @JoinColumn(name = "followed_id"), // The user being followed
            inverseJoinColumns = @JoinColumn(name = "follower_id") // The user who is following
    )
    private List<User> followers;

    @ManyToMany(mappedBy = "followers")
    private List<User> following;



}
