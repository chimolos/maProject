package com.second.maproject.posts.controller;

import com.second.maproject.posts.models.Post;
import com.second.maproject.posts.request.PostRequest;
import com.second.maproject.posts.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(value = "/user/post/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String createPost(@ModelAttribute PostRequest request) throws IOException {
        System.out.println("CREATING POST ");
        String url = postService.createPost(request);
//    public String createPost(PostRequest request, @RequestParam("image") MultipartFile file) throws IOException {
//
//        String url = postService.createPost(request, file);
//
        return "Post created successfully: File path: " + url;
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

}
