package Service;

import model.Application;
import model.User;
import repository.ApplicationRepository;
import repository.ReactionRepository;
import repository.UserRepository;

import java.util.List;

public class MatchmakingService {

    private final ApplicationRepository applicationRepository;
    private final ReactionRepository reactionRepository;

    public MatchmakingService(ApplicationRepository applicationRepository, ReactionRepository reactionRepository) {
        this.applicationRepository = applicationRepository;
        this.reactionRepository = reactionRepository;
    }

    public Application getNext(long userId) {
        return applicationRepository.getNextApplication(userId);
    }

    public boolean like(long userId, int applicationId) {

        reactionRepository.saveReaction(userId, applicationId, true);

        long targetUserId = applicationRepository.getApplicationOwner(applicationId);

        return reactionRepository.hasUserLikedMe(userId, targetUserId);
    }

    public void dislike(long userId, int applicationId) {
        reactionRepository.saveReaction(userId, applicationId, false);
    }

}
