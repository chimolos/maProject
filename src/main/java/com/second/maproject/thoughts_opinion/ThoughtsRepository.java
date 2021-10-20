package com.second.maproject.thoughts_opinion;

import com.second.maproject.posts.models.Post;
import com.second.maproject.posts.models.PostComment;
import com.second.maproject.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ThoughtsRepository extends JpaRepository<Thoughts_Opinion, Long> {
    void deleteByUserAndId(User users, Long id);
}
