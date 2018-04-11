package pl.tomihome.gamification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.tomihome.gamification.domain.GameStats;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    @Override
    public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {
        return null;
    }

    @Override
    public GameStats retrieveStatsForUser(Long userId) {
        return null;
    }
}
