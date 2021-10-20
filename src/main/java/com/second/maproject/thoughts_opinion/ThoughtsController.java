package com.second.maproject.thoughts_opinion;

import com.second.maproject.posts.models.Post;
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

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ThoughtsController {

    @Autowired
    ThoughtsService thoughtsService;

    @Autowired
    ThoughtsRepository thoughtsRepo;

    @Autowired
    UserRepository userRepo;

    @PostMapping(value = "/user/thoughts/create")
    public String addThought(@RequestParam("description") String description) {
       thoughtsService.addThought(description);
        JSONObject response = new JSONObject();
        response.put("msg", "Thought/Opinion added");
        return response.toString();
    }

    @PutMapping(value = "/user/thoughts/{id}/edit")
    public String editThought(@PathVariable Long id, @RequestParam("description") String description) {
        thoughtsService.editThought(id, description);
        JSONObject response = new JSONObject();
        response.put("msg", "Thought/Opinion edited");
        return response.toString();
    }

    @GetMapping("/all/thoughts")
    public List<Thoughts_Opinion> getAllThoughts() {
        return thoughtsService.getAllThoughts();
    }

    @GetMapping("/all/thought/{id}")
    public Thoughts_Opinion getThoughtById(@PathVariable Long id) {
        return thoughtsService.getThoughtById(id);
    }

    @Transactional
    @DeleteMapping("/user/thought/{id}/delete")
    public void deletePost(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        thoughtsRepo.deleteByUserAndId(user, id);
    }

}
