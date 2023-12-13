package ca.gbc.friendshipservice.boostrap;

import ca.gbc.friendshipservice.model.Friendship;
import ca.gbc.friendshipservice.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Data implements CommandLineRunner {

    private final FriendshipRepository friendshipRepository;

    @Override
    public void run(String... args){

        if(friendshipRepository.findByUsername(Long.valueOf("Venus")).isEmpty()){
            Friendship widgets = Friendship.builder()
                    .username(Long.valueOf("Venus"))
                    .friendName(Long.valueOf("James"))
                    .status("Requested")
                    .build();
            friendshipRepository.save(widgets);
        }

        if(friendshipRepository.findByUsername(Long.valueOf("William")).isEmpty()){
            Friendship widgets = Friendship.builder()
                    .username(Long.valueOf("William"))
                    .friendName(Long.valueOf("Harold"))
                    .status("Approved")
                    .build();
            friendshipRepository.save(widgets);

            Friendship secondWidget = Friendship.builder()
                    .username(Long.valueOf("James"))
                    .friendName(Long.valueOf("John"))
                    .status("Approved")
                    .build();
            friendshipRepository.save(secondWidget);
        }
    }
}
