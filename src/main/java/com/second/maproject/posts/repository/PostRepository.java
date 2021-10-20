package com.second.maproject.posts.repository;

import com.second.maproject.category.Category;
import com.second.maproject.posts.models.Post;
import com.second.maproject.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitle(String title);
    List<Post> findByAreaOfReport(Category category);
    Post findByUser(User user);
    void deleteByUserAndId(User users, Long id);

}
