package com.second.maproject.users.services;

import com.second.maproject.users.models.User;
import com.second.maproject.users.repository.RoleRepository;
import com.second.maproject.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final EmailVerificationService emailVerificationService;

    public void saveUser(User user, String appURL) throws MessagingException, UnsupportedEncodingException {
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        userRepo.save(user);
        emailVerificationService.sendVerificationEmail(user, appURL);
    }

//    public void addRoleToUser(String username, String roleName) {
//        User user = userRepo.findByUsername(username);
//        Role role = roleRepo.findByName(roleName).get();
//        user.getRoles().add(role);
//    }

    public User getUser(String username) {
        return userRepo.findByUsername(username);
    }

}
