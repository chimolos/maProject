package com.second.maproject.users.controllers;

import com.second.maproject.users.repository.UserRepository;
import com.second.maproject.users.models.User;
import com.second.maproject.users.requests.UpdatePasswordRequest;
import com.second.maproject.users.services.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PasswordResetController {

    @Autowired
    PasswordResetService resetService;

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/all/forgotPassword")
    public String forgotPassword(HttpServletRequest request, @RequestParam("email") String userEmail) throws MessagingException, UnsupportedEncodingException {
        return resetService.forgotPassword(request, userEmail);
    }

    @PutMapping("/all/resetPassword")
    public String resetPassword(@RequestParam String token, @RequestParam String password) {

        return resetService.resetPassword(token, password);
    }

    @PostMapping("/user/changePassword")
    public String changeUserPassword(@RequestBody UpdatePasswordRequest passwordRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepo.findByUsername(username);

        if (!encoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidParameterException("Password does not  match");
        }
        user.setPassword(encoder.encode(passwordRequest.getNewPassword()));
        userRepo.save(user);
        JSONObject response = new JSONObject();
        response.put("msg", "Password updated successfully");
        return response.toString();
    }
}
