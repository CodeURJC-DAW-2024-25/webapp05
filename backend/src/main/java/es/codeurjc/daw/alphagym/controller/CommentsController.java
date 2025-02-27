package es.codeurjc.daw.alphagym.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.daw.alphagym.model.Comment;
import es.codeurjc.daw.alphagym.service.CommentService;

@Controller
@RequestMapping("/comments")
public class CommentsController {

    private final CommentService commentService;

    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{type}/{id}")
    public String showComments(@PathVariable String type, @PathVariable Long id, Model model) {
        List<Comment> comments;
        if (type.equals("routine")) {
            comments = commentService.getCommentsByRoutine(id);
            model.addAttribute("type", "Routine");
        } else {
            comments = commentService.getCommentsByDiet(id);
            model.addAttribute("type", "Diet");
        }
        model.addAttribute("comments", comments);
        return "comments";
    }

    @PostMapping("/add")
    public String addComment(@RequestParam String commentTitle, @RequestParam String description,
                             @RequestParam String type, @RequestParam Long relatedId) {
        Comment comment = new Comment(commentTitle, description, type, relatedId);
        commentService.saveComment(comment);
        return "redirect:/comments/" + type + "/" + relatedId;
    }
    
}
