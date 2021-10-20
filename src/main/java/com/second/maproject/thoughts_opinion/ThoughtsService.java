package com.second.maproject.thoughts_opinion;

import com.second.maproject.posts.models.Post;
import com.second.maproject.posts.models.PostComment;
import com.second.maproject.posts.repository.PostCommentRepository;
import com.second.maproject.posts.repository.PostRepository;
import com.second.maproject.posts.service.CommentService;
import com.second.maproject.posts.service.PostService;
import com.second.maproject.users.models.User;
import com.second.maproject.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThoughtsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ThoughtsRepository thoughtsRepo;

    public void addThought(String description) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        Thoughts_Opinion thoughts = new Thoughts_Opinion();
        thoughts.setUser(user);
        thoughts.setDescription(description);

        thoughtsRepo.save(thoughts);
    }

    public void editThought(Long id, String description) {
        Thoughts_Opinion editThought = getThoughtById(id);

        editThought.setDescription(description);

        thoughtsRepo.save(editThought);
    }

    public List<Thoughts_Opinion> getAllThoughts() {
        return thoughtsRepo.findAll();
    }

    public Thoughts_Opinion getThoughtById(Long id) {
        Thoughts_Opinion thought = thoughtsRepo.findById(id)
                .orElseThrow(() -> new IllegalCallerException("Does not exist"));
        return thought;
    }
}
