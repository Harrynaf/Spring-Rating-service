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

class DbMapperTests {
    @Mock
    private DbMappers dbMapper;

    @Test
    void worksWithNulls() {
        Assert.isNull(dbMapper.fromDtoModel(null), "Should be null");
    }

    @Test
    void mapsCorrectly() {
        var ratingDTO = new RatingDTO();
        ratingDTO.setCreatedAt(Date.from(Instant.now().minusSeconds(100)));
        ratingDTO.setGivenRating(1);
        ratingDTO.setRatedEntity("none");
        ratingDTO.setRater("rater");

        var rating = new Rating();
        rating.setCreatedAt(new Date());
        rating.setGivenRating(ratingDTO.getGivenRating());
        rating.setRatedEntity(ratingDTO.getRatedEntity());
        rating.setRater(ratingDTO.getRater());


        when(dbMapper.fromDtoModel(ratingDTO)).thenReturn(rating);

        var db = dbMapper.fromDtoModel(ratingDTO);

        Assert.isTrue(db.getCreatedAt().compareTo(ratingDTO.getCreatedAt()) > 0, "Created date not auto assigned");
        // Use an appropriate delta for double comparison
        double delta = 0.001;
        Assert.isTrue(Math.abs(ratingDTO.getGivenRating() - db.getGivenRating()) < delta, "Not equal ratings");
        Assert.isTrue(ratingDTO.getRatedEntity().equals(db.getRatedEntity()), "Not equal entities");
        Assert.isTrue(ratingDTO.getRater().equals(db.getRater()), "Not equal raters");
    }
}
