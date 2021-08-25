package com.second.maproject.posts.controller;

import com.second.maproject.posts.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/user/comment/add/{postId}")
    public String addComment(@PathVariable Long postId, @RequestParam("review") String comment) {
        commentService.addComment(postId, comment);
        return "Your comment is successfully recorded";
    }

    @GetMapping("/all/comments/{postId}")
    public ResponseEntity<?> getCommentByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsPerPost(postId));
    }
}
