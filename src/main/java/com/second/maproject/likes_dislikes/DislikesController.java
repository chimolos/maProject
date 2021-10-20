package com.second.maproject.likes_dislikes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DislikesController {

    @Autowired
    DislikesService dislikesService;

    @PostMapping("/user/post/{postId}/dislike")
    public void dislikePost(@PathVariable Long postId) {
        dislikesService.dislikePost(postId);
    }

    @PostMapping("/user/thought/{thoughtId}/dislike")
    public void dislikeThought(@PathVariable Long thoughtId) {
        dislikesService.dislikeThought(thoughtId);
    }
}
