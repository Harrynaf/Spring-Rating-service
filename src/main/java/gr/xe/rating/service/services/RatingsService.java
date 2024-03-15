package gr.xe.rating.service.services;

import gr.xe.rating.service.mappers.DbMappers;
import gr.xe.rating.service.mappers.DtoMappers;
import gr.xe.rating.service.models.db.Rating;
import gr.xe.rating.service.models.dto.OverallRatingDTO;
import gr.xe.rating.service.models.dto.RatingDTO;
import gr.xe.rating.service.repositories.RatingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;

@AllArgsConstructor
@Service
public class RatingsService {

    private static final double DOUBLE_1 = 1.0;
    private static final double DOUBLE_0 = 0.0;
    private static final double DOUBLE_5 = 5.0;
    private static final double DOUBLE_01 = 0.1;
    private static final double DOUBLE_100 = 100.0;
    private DbMappers dbMapper;
    private DtoMappers dtoMapper;

    private RatingsRepository ratingsRepository;

    public RatingDTO createRating(RatingDTO ratingDTO) {
        if (ratingDTO.getRatedEntity() == null || ratingDTO.getGivenRating() < 0 || ratingDTO.getGivenRating() > 5) {
            // Handle validation error
            throw new IllegalArgumentException("Invalid rating or rated entity");
        }
        Rating rating = dbMapper.fromDtoModel(ratingDTO);
        rating = ratingsRepository.save(rating);
        return dtoMapper.fromDbModel(rating);
    }

    public Optional<Rating> getById(Long id) {
        return ratingsRepository.findById(id);
    }
    public List<Rating> getAllRatings() {
        return ratingsRepository.findAll();
    }

    @Cacheable("ratingsCache")
    public OverallRatingDTO getOverallRatingByRatedEntity(String ratedEntity) {
        Date startDate = calculateStartDateForRatingAlgorithm();

        // Fetch ratings for the given ratedEntity within the last 100 days
        List<Rating> ratings = ratingsRepository.findByRatedEntityAndCreatedAtAfter(ratedEntity, startDate);

        if (ratings.isEmpty()) {
            // Handle validation error
            throw new IllegalArgumentException("Invalid rating or rated entity");
        }

        // Calculate the overall rating based on the provided algorithm
        double weightedRatingsSum = 0;
        int numOfRatings = 0;
        for (Rating rating : ratings) {
            double weightedRating = calculateWeightedRating(rating);
            weightedRatingsSum += weightedRating;
            numOfRatings++;
        }

        // Calculate overall rating
        double overallRating = (weightedRatingsSum / numOfRatings) / 20;
        return OverallRatingDTO.builder().overallRating(overallRating).numOfRatings(numOfRatings).build();
    }

    private double calculateWeightedRating(Rating rating) {
        double ratingFactor = calculateRatingFactor(rating.getGivenRating());
        double ageFactor = calculateAgeFactor(rating.getCreatedAt());
        double raterFactor = rating.getRater() != null ? DOUBLE_1 : DOUBLE_01;

        return 100 * ratingFactor * ageFactor * raterFactor;
    }

    private Date calculateStartDateForRatingAlgorithm() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -100);
        return calendar.getTime();
    }

    private double calculateRatingFactor(double givenRating) {
        if (givenRating == DOUBLE_5) {
            return DOUBLE_1;
        } else if (givenRating >= 0 && givenRating < DOUBLE_5) {
            return givenRating / DOUBLE_5;
        } else {
            return DOUBLE_0;
        }
    }

    private double calculateAgeFactor(Date createdAt) {
        Date currentDate = new Date();
        long diffInMillisecs = currentDate.getTime() - createdAt.getTime();
        long diffInDays = diffInMillisecs / (1000 * 60 * 60 * 24);

        if (diffInDays < 0) {
            return DOUBLE_1;
        } else if (diffInDays <= 100) {
            return DOUBLE_1 - (diffInDays / DOUBLE_100);
        } else {
            return DOUBLE_0;
        }
    }
}
