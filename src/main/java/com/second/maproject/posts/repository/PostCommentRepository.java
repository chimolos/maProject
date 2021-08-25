package com.second.maproject.posts.repository;

import com.second.maproject.posts.models.Post;
import com.second.maproject.posts.models.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findAllByPost(Post post);
    long countByPost(Post post);
}
