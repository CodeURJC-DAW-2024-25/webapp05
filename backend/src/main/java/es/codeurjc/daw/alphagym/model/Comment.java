package es.codeurjc.daw.alphagym.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commentTitle;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    private String type; // "routine" o "diet"

    private Long relatedId; // ID de la rutina o dieta a la que pertenece

    public Comment() {}

    public Comment(String commentTitle, String description, String type, Long relatedId) {
        this.commentTitle = commentTitle;
        this.description = description;
        this.type = type;
        this.relatedId = relatedId;
    }

    public Long getId() { return id; }
    public String getCommentTitle() { return commentTitle; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public Long getRelatedId() { return relatedId; }

    public void setCommentTitle(String commentTitle) { this.commentTitle = commentTitle; }
    public void setDescription(String description) { this.description = description; }
    public void setType(String type) { this.type = type; }
    public void setRelatedId(Long relatedId) { this.relatedId = relatedId; }
}
