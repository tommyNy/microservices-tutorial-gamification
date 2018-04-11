package pl.tomihome.gamification.service;

import pl.tomihome.gamification.domain.GameStats;

public interface GameService {
    //Process a new attempt from a given user
    GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct);


    //Gets the game statistics for a given user
    GameStats retrieveStatsForUser(Long userId);
}
