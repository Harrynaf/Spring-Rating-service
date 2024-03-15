package gr.xe.rating.service.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Builder
@Getter
@Setter
public class OverallRatingDTO {
    private int numOfRatings;
    private double overallRating;

    public OverallRatingDTO(int numOfRatings, double overallRating) {
        this.numOfRatings = numOfRatings;
        this.overallRating = overallRating;
    }
}
