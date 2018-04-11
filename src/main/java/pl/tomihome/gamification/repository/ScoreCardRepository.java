package pl.tomihome.gamification.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.tomihome.gamification.domain.LeaderBoardRow;
import pl.tomihome.gamification.domain.ScoreCard;

import java.util.List;

public interface ScoreCardRepository extends CrudRepository<ScoreCard, Long> {

    //Gets the total score for a given user, being the sum of
    //the scores of all his ScoreCards.
    @Query("SELECT SUM(s.score) FROM ScoreCard s WHERE s.userId = :userId GROUP BY s.userId")
    int getTotalScoreForUser(@Param("userId")Long userId);

    //Retrieves a list of LeaderBoardRows representing
    //the Leader Board of users and their total score.
    @Query("SELECT NEW LeaderBoardRow(s.userId, SUM(s.score)) FROM ScoreCard s GROUP BY s.userId ORDER BY SUM(s.score) DESC")
    LeaderBoardRow findFirst10();

    //Retrieves all the ScoreCards for a given user,
    //identified by his user id.
    List<ScoreCard> findByUserIdOrderByScoreTimestampDesc(Long userId);
}
