/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package es.codeurjc.daw.alphagym.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.alphagym.model.Comment;
import es.codeurjc.daw.alphagym.repository.CommentRepository;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TrainingCommentService trainingCommentService;
    @Autowired
    private NutritionCommentService nutritionCommentService;
    @Autowired
    private UserService userService;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    /*PENDIENTE DE VERIFICACIÓN */
    public List<Comment> getCommentsByRoutine(Long routineId) {
        return commentRepository.findByTypeAndRelatedId("routine", routineId);
    }

    public List<Comment> getCommentsByDiet(Long dietId) {
        return commentRepository.findByTypeAndRelatedId("diet", dietId);
    }
    /* 
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
    */

    public void saveComment(Comment comment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
