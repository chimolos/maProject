package com.second.maproject.posts.controller;

import com.second.maproject.posts.models.Post;
import com.second.maproject.posts.repository.PostCommentRepository;
import com.second.maproject.posts.repository.PostRepository;
import com.second.maproject.posts.request.PostRequest;
import com.second.maproject.posts.service.PostService;
import com.second.maproject.users.models.User;
import com.second.maproject.users.repository.UserRepository;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepo;

    @Autowired
    UserRepository userRepo;

    @PostMapping(value = "/user/post/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createPost(@ModelAttribute PostRequest request) throws IOException {
        String url = postService.createPost(request);
//    public String createPost(PostRequest request, @RequestParam("image") MultipartFile file) throws IOException {
//
//        String url = postService.createPost(request, file);
//
        JSONObject response = new JSONObject();
        response.put("msg", "Post created successfully: File path: " + url);
        return response.toString();
    }

    @PutMapping("/user/post/{id}/edit")
    public String editPost(@PathVariable Long id, @RequestBody PostRequest request) {
        postService.editPost(id, request);
        JSONObject response = new JSONObject();
        response.put("msg", "Post edited successfully");
        return response.toString();
    }

    @GetMapping("/all/posts")
    public List<Post> getAllPosts() {
        return postService.getAllPost();
    }

    @GetMapping("/user/post/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("/all/posts/{catId}")
    public ResponseEntity<?> getPostsByCategory(@PathVariable Long catId) {

        return ResponseEntity.ok(postService.getPostsPerCategory(catId));
    }

    @Transactional
    @DeleteMapping("/user/post/{id}/delete")
    public void deletePost(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        postRepo.deleteByUserAndId(user, id);
    }

}
