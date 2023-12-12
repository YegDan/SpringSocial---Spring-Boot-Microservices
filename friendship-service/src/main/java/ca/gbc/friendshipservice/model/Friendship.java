package ca.gbc.friendshipservice.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "friendship")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long username;
    private Long friendName;
    private String status;

}
