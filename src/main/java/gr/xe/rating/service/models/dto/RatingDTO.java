package gr.xe.rating.service.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class RatingDTO {
    private double givenRating;

    private String ratedEntity;

    private String rater;

    private Date createdAt;

}
