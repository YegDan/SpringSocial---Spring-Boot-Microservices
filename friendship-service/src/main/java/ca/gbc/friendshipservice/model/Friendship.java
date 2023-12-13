package ca.gbc.friendshipservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "__friendship")
public class Friendship {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private Long recipientId;
    private Long senderId;



}
