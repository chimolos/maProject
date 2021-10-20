package com.second.maproject.users.controllers;

import com.second.maproject.users.requests.ProfileRequest;
import com.second.maproject.users.services.UserProfileService;
import com.second.maproject.users.models.User;
import com.second.maproject.users.repository.UserRepository;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user/profile")
public class UserProfileController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserProfileService userProfileService;


    @GetMapping
    public User viewProfile() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        return user;
    }

    @PutMapping("/update")
    public String updateProfile(ProfileRequest request, @RequestParam("profilePic")MultipartFile file) throws IOException {

        String url = userProfileService.updateProfile(request, file);
        JSONObject response = new JSONObject();
        response.put("msg", "Profile successfully updated: " + url);
        return response.toString();
    }

}
