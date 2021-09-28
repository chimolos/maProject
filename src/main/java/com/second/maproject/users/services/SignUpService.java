package com.second.maproject.users.services;

import com.second.maproject.users.requests.SignUpRequest;
import com.second.maproject.users.models.Role;
import com.second.maproject.users.models.User;
import com.second.maproject.users.repository.RoleRepository;
import com.second.maproject.users.repository.UserRepository;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class SignUpService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    public String registerUser(@RequestBody SignUpRequest signUpRequest, HttpServletRequest request ) throws MessagingException, UnsupportedEncodingException {
        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalStateException("Error: Username is already taken!");
        }
        if (userRepo.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalStateException("Error: Email is already in use!");
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Collection<String> strRoles = signUpRequest.getRole();
        Collection<Role> roles = new ArrayList<>();

        if (strRoles == null) {
            Role userRole = roleRepo.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {

                if ("admin".equals(role)) {
                    Role adminRole = roleRepo.findByName("ADMIN")
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepo.findByName("USER")
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userService.saveUser(user, getSiteURL(request));

//        profile.setUser(user);
//        userProRepo.save(profile);

        JSONObject response = new JSONObject();
        response.put("msg", "User registered successfully");
        return response.toString();
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
