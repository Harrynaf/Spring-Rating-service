package gr.xe.rating.service.mappers;

import gr.xe.rating.service.models.db.Rating;
import gr.xe.rating.service.models.dto.RatingDTO;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface DtoMappers {
    RatingDTO fromDbModel(Rating rating);
}
