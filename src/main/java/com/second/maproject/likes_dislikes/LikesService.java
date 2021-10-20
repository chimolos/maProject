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
public class LikesService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    LikesRepository likesRepo;

    @Autowired
    PostRepository postRepo;

    @Autowired
    ThoughtsRepository thoughtsRepo;

    @Autowired
    PostService postService;

    @Autowired
    ThoughtsService thoughtsService;

    public void likePost(Long postId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        Post post = postService.getPostById(postId);

        Likes like = likesRepo.findByUserIdAndPostId(user.getId(), postId);
        if (like != null) {
            likesRepo.delete(like);
        } else {
            Likes newLike = new Likes();
            newLike.setPostId(postId);
            newLike.setUserId(user.getId());
            likesRepo.save(newLike);
        }

        int likes = likesRepo.countByPostId(postId);
        post.setNumberOfLikes(likes);
        postRepo.save(post);
    }

    public void likeThought(Long thoughtId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        Thoughts_Opinion thought = thoughtsService.getThoughtById(thoughtId);

        Likes like = likesRepo.findByUserIdAndThoughtId(user.getId(), thoughtId);
        if (like != null) {
            likesRepo.delete(like);
        } else {
            Likes newLike = new Likes();
            newLike.setThoughtId(thoughtId);
            newLike.setUserId(user.getId());
            likesRepo.save(newLike);
        }

        int likes = likesRepo.countByThoughtId(thoughtId);
        thought.setNumberOfLikes(likes);
        thoughtsRepo.save(thought);
    }
}
