package com.second.maproject.users.security.service;

import com.second.maproject.users.repository.UserRepository;
import com.second.maproject.users.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username);

        if (user != null) {
            return UserDetailsImpl.build(user);
        } else {
            User user1 = userRepo.findByEmail(username);

            if (user1 != null) {
                return UserDetailsImpl.build(user1);
            }
            throw new UsernameNotFoundException("User Not Found with username: " + username);
        }
    }
}

