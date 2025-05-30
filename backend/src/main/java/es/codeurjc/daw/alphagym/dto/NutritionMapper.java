package es.codeurjc.daw.alphagym.dto;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import es.codeurjc.daw.alphagym.model.Nutrition;


@Mapper(componentModel = "spring")
public interface NutritionMapper {

    @Mapping(target = "userId", source = "user.id")
    UniqueNutritionDTO toUniqueDTO(Nutrition nutrition);

    @Mapping(target = "userId", source = "user.id")
    NutritionDTO toDTO(Nutrition nutrition);

    @Mapping(target = "userId", source = "user.id")
    List<NutritionDTO> toDTOs(Collection<Nutrition> nutritions);

    @Mapping(target = "imgNutrition", ignore = true)
    @Mapping(target = "user", ignore = true)
    Nutrition toDomain(NutritionDTO nutritionDTO);
}
