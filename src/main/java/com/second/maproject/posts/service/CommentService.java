package com.second.maproject.posts.service;

import com.second.maproject.posts.models.PostComment;

import java.util.List;

public interface CommentService {
    void addComment(Long postId, String comment);
    List<PostComment> getCommentsPerPost(Long postId);
}
