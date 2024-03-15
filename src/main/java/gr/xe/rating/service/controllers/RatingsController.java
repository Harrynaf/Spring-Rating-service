package gr.xe.rating.service.controllers;

import gr.xe.rating.service.mappers.DtoMappers;
import gr.xe.rating.service.models.db.Rating;
import gr.xe.rating.service.models.dto.OverallRatingDTO;
import gr.xe.rating.service.models.dto.RatingDTO;
import gr.xe.rating.service.services.RatingsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/ratings")
@AllArgsConstructor
public class RatingsController {

    private DtoMappers dtoMapper;

    private RatingsService ratingsService;

    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody RatingDTO ratingDTO) {
        try {
            RatingDTO createdRating = ratingsService.createRating(ratingDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRating);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<RatingDTO>> getAllRatings() {
        List<RatingDTO> ratingDTOS = new ArrayList<>();
        for (Rating rating:ratingsService.getAllRatings())
        {ratingDTOS.add(dtoMapper.fromDbModel(rating));}
        return ResponseEntity.ok(ratingDTOS);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<RatingDTO> getRating(@PathVariable("id") Long id) {
        Optional<Rating> optionalRating = ratingsService.getById(id);
        RatingDTO ratingDTO = optionalRating.map(rating -> dtoMapper.fromDbModel(rating)).orElse(null);
        return ratingDTO != null ? ResponseEntity.ok(ratingDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{rated_entity}")
    public ResponseEntity<?> getOverallRatingByRatedEntity(@PathVariable("rated_entity") String ratedEntity) {
        try {
            OverallRatingDTO overallRatingDTO = ratingsService.getOverallRatingByRatedEntity(ratedEntity);
            return ResponseEntity.ok(overallRatingDTO);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
