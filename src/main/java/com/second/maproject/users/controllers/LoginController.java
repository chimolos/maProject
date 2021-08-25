package com.second.maproject.users.controllers;

import com.second.maproject.users.services.RefreshTokenService;
import com.second.maproject.users.services.UserService;
import com.second.maproject.users.repository.UserRepository;
import com.second.maproject.users.requests.LoginRequest;
import com.second.maproject.users.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;


//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/all")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) throws IOException {
        return loginService.authenticateUser(loginRequest, response);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        refreshTokenService.refreshToken(request, response);
    }

//    @GetMapping("/listUsers")
//    public List<User> listAll() {
//        return userRepo.findAll();
//    }
}
