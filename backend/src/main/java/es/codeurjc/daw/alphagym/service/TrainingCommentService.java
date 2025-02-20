package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;
import java.util.Optional;


@Service
public class TrainingCommentService {

    @Autowired
    private TrainingCommentRepository trainingCommentRepository;

    public TrainingComment createTrainingComment(TrainingComment trainingComment, User user) {
        TrainingComment trainingComment1 = new TrainingComment(trainingComment.getComment(),trainingComment.getAuthor(),trainingComment.getTitle());
        trainingCommentRepository.save(trainingComment1);
        return trainingComment1;
    }
}
