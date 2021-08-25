package com.second.maproject.posts.service;

import com.second.maproject.posts.request.PostRequest;
import com.second.maproject.posts.models.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    void createPost(PostRequest request, MultipartFile file) throws IOException;
    List<Post> getAllPost();
    Post getPostById(Long id);
    List<Post> getPostsPerCategory(Long catId);
}
