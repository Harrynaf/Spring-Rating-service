package gr.xe.rating.service.repositories;

import gr.xe.rating.service.models.db.Rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
@Repository
public interface RatingsRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByRatedEntityAndCreatedAtAfter(String ratedEntity, Date startDate);
    void deleteByCreatedAtBefore(Date date);
}
