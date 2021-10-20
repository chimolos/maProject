package com.second.maproject.posts.service;

import com.second.maproject.posts.models.PostComment;

import java.util.List;

public interface CommentService {
    void addComment(Long postId, String comment);
//    void editComment(Long commentId, String comment);
//    PostComment getCommentById(Long commentId);
    List<PostComment> getCommentsPerPost(Long postId);
}
