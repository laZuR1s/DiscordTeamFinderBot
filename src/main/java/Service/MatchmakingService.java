package Service;

import model.User;
import repository.UserRepository;

import java.util.List;

public class MatchmakingService {

    private final UserRepository userRepository;

    public MatchmakingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findTeammates(String game, String requesterId){
        return userRepository.findByGame(game).stream()
                .filter(user -> !user.getDiscordID().equals(requesterId))
                .toList();
    }

}
