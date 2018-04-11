package pl.tomihome.gamification.repository;

import org.springframework.data.repository.CrudRepository;
import pl.tomihome.gamification.domain.Badge;

import java.util.List;

public interface BadgeCardRepository extends CrudRepository<BadgeCardRepository, Long> {
    List<Badge> findByUserIdOrderByBadgeTimestampDesc(Long userId);
}
