package pl.tomihome.gamification.service;

import pl.tomihome.gamification.domain.LeaderBoardRow;

import java.util.List;

public interface LeaderBoardService {
    //Retrieves the current leader board with the top scoreusers
    List<LeaderBoardRow> getCurrentLeaderBoard();
}
