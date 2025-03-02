package es.codeurjc.daw.alphagym.service;


import es.codeurjc.daw.alphagym.model.NutritionComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.NutritionCommentRepository;
import es.codeurjc.daw.alphagym.repository.NutritionRepository;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

@Service
public class NutritionCommentService {
    
    @Autowired
    private NutritionCommentRepository nutritionCommentRepository;
    @Autowired
    private UserService userService;


    @PostConstruct
    public void nutritionCommentConstructor() {
        NutritionComment variedad = new NutritionComment("Es importante comer variado", "Variedad",1L);

        NutritionComment hidratación = new NutritionComment("Es necesario beber mucha agua", "Hidratación",1L);
        NutritionComment entidad1 = new NutritionComment("Comentario 1", "Entrenamiento Básico", 3L);
        NutritionComment entidad2 = new NutritionComment("Comentario 2", "Fuerza", 2L);
        NutritionComment entidad3 = new NutritionComment("Comentario 3", "Cardio Intenso", 1L);
        NutritionComment entidad4 = new NutritionComment("Comentario 4", "Rutina de Piernas", 1L);
        NutritionComment entidad5 = new NutritionComment("Comentario 5", "Espalda y Bíceps", 2L);
        NutritionComment entidad6 = new NutritionComment("Comentario 6", "Pecho y Tríceps", 1L);
        NutritionComment entidad7 = new NutritionComment("Comentario 7", "Entrenamiento Funcional", 1L);
        NutritionComment entidad8 = new NutritionComment("Comentario 8", "HIIT", 1L);
        NutritionComment entidad9 = new NutritionComment("Comentario 9", "Entrenamiento Full Body", 1L);
        NutritionComment entidad10 = new NutritionComment("Comentario 10", "CrossFit", 1L);
        NutritionComment entidad11 = new NutritionComment("Comentario 11", "Pilates", 3L);
        NutritionComment entidad12 = new NutritionComment("Comentario 12", "Yoga", 1L);
        NutritionComment entidad13 = new NutritionComment("Comentario 13", "Abdominales", 1L);
        NutritionComment entidad14 = new NutritionComment("Comentario 14", "Glúteos y Piernas", 1L);
        NutritionComment entidad15 = new NutritionComment("Comentario 15", "Rutina de Flexibilidad", 1L);


        createNutritionComment(variedad);
        createNutritionComment(hidratación);
        createNutritionComment(entidad1);
        createNutritionComment(entidad2);
        createNutritionComment(entidad3);
        createNutritionComment(entidad4);
        createNutritionComment(entidad5);
        createNutritionComment(entidad6);
        createNutritionComment(entidad7);
        createNutritionComment(entidad8);
        createNutritionComment(entidad9);
        createNutritionComment(entidad10);
        createNutritionComment(entidad11);
        createNutritionComment(entidad12);
        createNutritionComment(entidad13);
        createNutritionComment(entidad14);
        createNutritionComment(entidad15);
    }


    public List<NutritionComment> getAllNutritionComments() {
        List<NutritionComment> listNutritionComment = nutritionCommentRepository.findAll();
        return listNutritionComment.isEmpty() ? null : listNutritionComment;
    }

    public List<NutritionComment> getNutritionComments(Long nutritionId){
        List<NutritionComment> listNutritionComments = nutritionCommentRepository.findByNutritionId(nutritionId);
        return listNutritionComments.isEmpty() ? null : listNutritionComments;
    }

    public void createNutritionComment(NutritionComment nutritionComment) {

        nutritionCommentRepository.save(nutritionComment);
    }


}
