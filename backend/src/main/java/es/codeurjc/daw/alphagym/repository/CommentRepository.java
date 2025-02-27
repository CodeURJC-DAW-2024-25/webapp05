/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package es.codeurjc.daw.alphagym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.daw.alphagym.model.Comment;
import es.codeurjc.daw.alphagym.model.NutritionComment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTypeAndRelatedId(String type, Long relatedId);

    public void save(NutritionComment comment);

    
}
