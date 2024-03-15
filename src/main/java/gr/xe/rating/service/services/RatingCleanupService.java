package gr.xe.rating.service.services;

import gr.xe.rating.service.repositories.RatingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;

@Service
public class RatingCleanupService {

    @Autowired
    private RatingsRepository ratingsRepository;

    @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight
    public void removeOldRatings() {
        Date startDate = calculateStartDateForRatingCleanup();

        // Remove old ratings from the database
        ratingsRepository.deleteByCreatedAtBefore(startDate);
    }

    private Date calculateStartDateForRatingCleanup() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -100); // Subtract 100 days
        return calendar.getTime();
    }
}
