package es.codeurjc.daw.alphagym.dto;

import java.util.Collection;
import java.util.List;

import es.codeurjc.daw.alphagym.model.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    @Mapping(target = "userId", source = "user.id")
    UniqueTrainingDTO toUniqueDTO(Training training);

    @Mapping(target = "userId", source = "user.id")
    TrainingDTO toDTO(Training training);

    @Mapping(target = "userId", source = "user.id")
    List<TrainingDTO> toDTOs(Collection<Training> trainings);

    @Mapping(target = "imgTraining", ignore = true)
    @Mapping(target = "user", ignore = true)
    Training toDomain(TrainingDTO trainingDTO);

}
