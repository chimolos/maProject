package com.second.maproject.posts.service;

import com.second.maproject.posts.models.Post;
import com.second.maproject.posts.models.PostComment;
import com.second.maproject.posts.repository.PostCommentRepository;
import com.second.maproject.posts.repository.PostRepository;
import com.second.maproject.users.models.User;
import com.second.maproject.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private UserRepository userRepo;

    @Autowired
    PostService postService;

    @Autowired
    PostCommentRepository commentRepo;

    @Autowired
    PostRepository postRepo;

    @Override
    public void addComment(Long postId, String comment) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        Post post = postService.getPostById(postId);

        PostComment postComment = new PostComment();
        postComment.setUser(user);
        postComment.setComment(comment);

        postComment.setPost(post);
//        post.addComment(postComment);

        commentRepo.save(postComment);
        int number = commentRepo.countByPost(post);
        post.setNumberOfComments(number);
        postRepo.save(post);
    }

//    @Override
//    public void editComment(Long commentId, String comment) {
//        PostComment editComment = getCommentById(commentId);
//
//        editComment.setComment(comment);
//        commentRepo.save(editComment);
//    }

//    @Override
//    public PostComment getCommentById(Long commentId) {
//        PostComment comment = commentRepo.findById(commentId)
//                .orElseThrow(() -> new IllegalCallerException("Post with id does not exist"));
//        return comment;
//    }

    @Override
    public List<PostComment> getCommentsPerPost(Long postId) {
        Post post = postService.getPostById(postId);

        return commentRepo.findAllByPost(post);
    }
}
