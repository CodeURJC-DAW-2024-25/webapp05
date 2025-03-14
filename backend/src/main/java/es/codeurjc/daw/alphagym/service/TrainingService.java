package es.codeurjc.daw.alphagym.service;

import es.codeurjc.daw.alphagym.model.Training;
import es.codeurjc.daw.alphagym.model.TrainingComment;
import es.codeurjc.daw.alphagym.model.User;
import es.codeurjc.daw.alphagym.repository.TrainingCommentRepository;
import es.codeurjc.daw.alphagym.repository.TrainingRepository;
import es.codeurjc.daw.alphagym.repository.UserRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TrainingCommentRepository trainingCommentRepository;
    @Autowired
    private UserRepository userRepository;

    public Training createTraining(Training training, User user) throws SQLException, IOException {
        Training newTraining = new Training(training.getName(),training.getIntensity(),training.getDuration(),training.getGoal(),training.getDescription());
        newTraining.setUser(user);
        if (training.getImgTraining()!=null){
            newTraining.setImgTraining(training.getImgTraining());
        } else {
            ClassPathResource imgFileDefault = new ClassPathResource("static/images/emptyImage.png");
            byte[] imageBytesDefault = Files.readAllBytes(imgFileDefault.getFile().toPath());
            Blob imageBlobDefault = new SerialBlob(imageBytesDefault);
            newTraining.setImgTraining(imageBlobDefault);
        }
        trainingRepository.save(newTraining);
        return newTraining;
    }

    public List<Training> getAllTrainings(){
        List<Training> listTraining = trainingRepository.findAll();
        return listTraining.isEmpty() ? null : listTraining;
    }

    public Training getTraining(Long id){
        Optional<Training> theRoutine = trainingRepository.findById(id);
        if (theRoutine.isPresent()) {
            return theRoutine.get();
        } else {
            return null;
        }
    }

    public Training updateRoutine(Long routineId, Training newTrainingData, User user) {
        Optional<Training> existingTrainingOpt = trainingRepository.findById(routineId);

        if (existingTrainingOpt.isPresent()) {
            Training existingTraining = existingTrainingOpt.get();

            existingTraining.setName(newTrainingData.getName());
            existingTraining.setIntensity(newTrainingData.getIntensity());
            existingTraining.setDuration(newTrainingData.getDuration());
            existingTraining.setGoal(newTrainingData.getGoal());
            existingTraining.setDescription(newTrainingData.getDescription());

            if (newTrainingData.getImgTraining() != null) {
                existingTraining.setImgTraining(newTrainingData.getImgTraining());
                existingTraining.setImage(true);
            }

            if (existingTraining.getUser() != null) {
                existingTraining.setUser(user);
            }

            return trainingRepository.save(existingTraining);
        }

        return null;
    }
    public Optional<Training> findById(Long id) {
        return trainingRepository.findById(id);
    }

    public Training deleteRoutine(Long id){
        Optional<Training> theRoutine = trainingRepository.findById(id);
        if (theRoutine.isPresent()) {
            Training training = theRoutine.get();

            List<User> usersWithTraining = userRepository.findByTrainingsContaining(training);
            for (User user : usersWithTraining) {
                user.getTrainings().remove(training);
                userRepository.save(user); 
            }

            List<TrainingComment> trainingComments = trainingCommentRepository.findByTrainingId(id);

            for (TrainingComment trainingComment : trainingComments) {
                trainingCommentRepository.delete(trainingComment);

            }
            trainingRepository.delete(training);
            return training;
        }
        return null;
    }

    public void subscribeTraining(Long trainingId , User user) {
        Optional<Training> training = trainingRepository.findById(trainingId);
        if (training.isPresent()) {
            user.getTrainings().add(training.get());
            userRepository.save(user);
        }
    }

    public void unsubscribeTraining(Long trainingId, User user) {
        Optional<Training> training = trainingRepository.findById(trainingId);
        if (training.isPresent()) {
            user.getTrainings().remove(training.get());
            userRepository.save(user);
        }
    }

    public List<Training> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return trainingRepository.findAll(); // if there is no search, return all
        }
        return trainingRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Long trainingId, Authentication authentication) {
        return trainingRepository.findWithUserById(trainingId)
                .map(training -> {
                    User user = training.getUser();
                    return user != null && authentication.getName().equals(user.getEmail());
                })
                .orElse(false);
    }



}
