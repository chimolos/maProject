package com.second.maproject.likes_dislikes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LikesController {

    @Autowired
    LikesService likesService;

    @PostMapping("/user/post/{postId}/like")
    public void likePost(@PathVariable Long postId) {
        likesService.likePost(postId);
    }

    @PostMapping("/user/thought/{thoughtId}/like")
    public void likeThought(@PathVariable Long thoughtId) {
        likesService.likeThought(thoughtId);
    }
}
