package com.second.maproject.likes_dislikes;

import com.second.maproject.posts.models.Post;
import com.second.maproject.posts.repository.PostRepository;
import com.second.maproject.posts.service.PostService;
import com.second.maproject.thoughts_opinion.ThoughtsRepository;
import com.second.maproject.thoughts_opinion.ThoughtsService;
import com.second.maproject.thoughts_opinion.Thoughts_Opinion;
import com.second.maproject.users.models.User;
import com.second.maproject.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class DislikesService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    DislikesRepository dislikesRepo;

    @Autowired
    PostRepository postRepo;

    @Autowired
    ThoughtsRepository thoughtsRepo;

    @Autowired
    PostService postService;

    @Autowired
    ThoughtsService thoughtsService;

    public void dislikePost(Long postId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        Post post = postService.getPostById(postId);

        Dislikes dislike = dislikesRepo.findByUserIdAndPostId(user.getId(), postId);
        if (dislike != null) {
            dislikesRepo.delete(dislike);
        } else {
            Dislikes newDislike = new Dislikes();
            newDislike.setPostId(postId);
            newDislike.setUserId(user.getId());
            dislikesRepo.save(newDislike);
        }

        int dislikes = dislikesRepo.countByPostId(postId);
        post.setNumberOfDislikes(dislikes);
        postRepo.save(post);
    }

    public void dislikeThought(Long thoughtId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        Thoughts_Opinion thought = thoughtsService.getThoughtById(thoughtId);

        Dislikes dislike = dislikesRepo.findByUserIdAndThoughtId(user.getId(), thoughtId);
        if (dislike != null) {
            dislikesRepo.delete(dislike);
        } else {
            Dislikes newDislike = new Dislikes();
            newDislike.setThoughtId(thoughtId);
            newDislike.setUserId(user.getId());
            dislikesRepo.save(newDislike);
        }

        int dislikes = dislikesRepo.countByThoughtId(thoughtId);
        thought.setNumberOfDislikes(dislikes);
        thoughtsRepo.save(thought);
    }
}
