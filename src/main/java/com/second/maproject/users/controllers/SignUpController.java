package com.second.maproject.users.controllers;

import com.second.maproject.users.requests.SignUpRequest;
import com.second.maproject.users.services.EmailVerificationService;
import com.second.maproject.users.services.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/all")
public class SignUpController {
    @Autowired
    SignUpService signUpService;

    @Autowired
    EmailVerificationService emailVerificationService;

    @PostMapping("/signup")
    public String registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws MessagingException, UnsupportedEncodingException {
        return signUpService.registerUser(signUpRequest);
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (emailVerificationService.verify(code)) {
            return "verification successful";
        } else {
            return "verification failed";
        }

    }
}
