package com.second.maproject.posts.controller;

import com.second.maproject.posts.repository.PostCommentRepository;
import com.second.maproject.posts.request.PostRequest;
import com.second.maproject.posts.service.CommentService;
import com.second.maproject.users.models.User;
import com.second.maproject.users.repository.UserRepository;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    PostCommentRepository commentRepo;

    @Autowired
    UserRepository userRepo;

    @PostMapping("/user/comment/add/{postId}")
    public String addComment(@PathVariable Long postId, @RequestParam("review") String comment) {
        commentService.addComment(postId, comment);
        JSONObject response = new JSONObject();
        response.put("msg", "Your comment is successfully recorded");
        return response.toString();
    }

//    @PutMapping("/user/comment/{commentId}/edit")
//    public String editComment(@PathVariable Long commentId, @RequestParam("review") String comment) {
//        commentService.editComment(commentId, comment);
//        JSONObject response = new JSONObject();
//        response.put("msg", "Comment edited successfully");
//        return response.toString();
//    }

    @GetMapping("/all/comments/{postId}")
    public ResponseEntity<?> getCommentByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsPerPost(postId));
    }

    @Transactional
    @DeleteMapping("/user/comment/{id}/delete")
    public void deleteComment(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        commentRepo.deleteByUserAndId(user, id);
    }
}
