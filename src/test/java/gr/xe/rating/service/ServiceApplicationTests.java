package gr.xe.rating.service;

import gr.xe.rating.service.mappers.DbMappers;
import gr.xe.rating.service.mappers.DtoMappers;
import gr.xe.rating.service.models.db.Rating;
import gr.xe.rating.service.models.dto.OverallRatingDTO;
import gr.xe.rating.service.models.dto.RatingDTO;
import gr.xe.rating.service.repositories.RatingsRepository;
import gr.xe.rating.service.services.RatingsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceApplicationTests {
	@Mock
	private DbMappers dbMapper;
	@Mock
	private DtoMappers dtoMapper;
	@Mock
	private RatingsRepository ratingsRepository;

	@InjectMocks
	private RatingsService ratingsService;

	@Test
	void testCreateRating_ValidRating() {
		RatingDTO ratingDTO = new RatingDTO();
		ratingDTO.setRatedEntity("Entity");
		ratingDTO.setGivenRating(4);

		Rating rating = new Rating();
		rating.setRatedEntity(ratingDTO.getRatedEntity());
		rating.setGivenRating(ratingDTO.getGivenRating());

		when(dbMapper.fromDtoModel(ratingDTO)).thenReturn(rating);
		when(ratingsRepository.save(any(Rating.class))).thenReturn(rating);
		when(dtoMapper.fromDbModel(rating)).thenReturn(ratingDTO);

		RatingDTO result = ratingsService.createRating(ratingDTO);

		assertNotNull(result);
		assertEquals(ratingDTO, result);
	}

	@Test
	void testCreateRating_InvalidRating() {
		RatingDTO ratingDTO = new RatingDTO(); // no rated entity or rating value
		assertThrows(IllegalArgumentException.class, () -> ratingsService.createRating(ratingDTO));

		ratingDTO.setRatedEntity("Entity");
		ratingDTO.setGivenRating(-1); // invalid rating value
		assertThrows(IllegalArgumentException.class, () -> ratingsService.createRating(ratingDTO));

		ratingDTO.setGivenRating(6); // invalid rating value
		assertThrows(IllegalArgumentException.class, () -> ratingsService.createRating(ratingDTO));
	}

	@Test
	void testGetOverallRatingByRatedEntity() {
		String ratedEntity = "TestEntity";
		Date createdAt = new Date(); // Provide a valid createdAt date value

		// Create a mock list of Rating objects
		List<Rating> mockRatings = List.of(createTestRating(2.5, "TestRater"),
				createTestRating(1.5, "TestRater1"));

		// Mocking the behavior of repository
		when(ratingsRepository.findByRatedEntityAndCreatedAtAfter(anyString(), any()))
				.thenReturn(mockRatings);

		// Invoking the service method
		OverallRatingDTO result = ratingsService.getOverallRatingByRatedEntity(ratedEntity);

		// Verifying the result
		assertNotNull(result);
		assertEquals(2, result.getNumOfRatings());
		assertEquals(2.0, result.getOverallRating());
	}


	private Rating createTestRating(double givenRating, String rater) {
		Rating rating = new Rating();
		rating.setGivenRating(givenRating);
		rating.setRatedEntity("TestEntity");
		rating.setCreatedAt(new Date());
		rating.setRater(rater);
		return rating;
	}

}
