package com.second.maproject.likes_dislikes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DislikesRepository extends JpaRepository<Dislikes, Long> {
    Dislikes findByUserIdAndPostId(Long userId, Long postId);
    Dislikes findByUserIdAndThoughtId(Long userId, Long thoughtId);
    int countByPostId(Long id);
    int countByThoughtId(Long id);
}
