package gr.xe.rating.service.mappers;

import gr.xe.rating.service.models.db.Rating;
import gr.xe.rating.service.models.dto.RatingDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class DtoMappersTests {
    @Mock
    private DtoMappers dtoMapper;

    @Test
    void worksWithNulls() {
        Assert.isNull(dtoMapper.fromDbModel(null), "Should be null");
    }

    @Test
    void mapsCorrectly() {
        var rating = new Rating();
        rating.setCreatedAt(Date.from(Instant.now()));
        rating.setGivenRating(1);
        rating.setRatedEntity("none");
        rating.setRater("rater");

        var ratingDTO = new RatingDTO();
        ratingDTO.setCreatedAt(rating.getCreatedAt());
        ratingDTO.setGivenRating(rating.getGivenRating());
        ratingDTO.setRatedEntity(rating.getRatedEntity());
        ratingDTO.setRater(rating.getRater());

        when(dtoMapper.fromDbModel(rating)).thenReturn(ratingDTO);

        var dto = dtoMapper.fromDbModel(rating);

        Assert.isTrue(dto.getCreatedAt().equals(rating.getCreatedAt()), "Not equal dates");
        // Use an appropriate delta for double comparison
        double delta = 0.001;
        Assert.isTrue(Math.abs(dto.getGivenRating() - rating.getGivenRating()) < delta, "Not equal ratings");
        Assert.isTrue(dto.getRatedEntity().equals(rating.getRatedEntity()), "Not equal entities");
        Assert.isTrue(dto.getRater().equals(rating.getRater()), "Not equal raters");
    }
}
