package com.second.maproject;

import com.second.maproject.users.models.Role;
import com.second.maproject.users.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class RoleCommandLineRunner implements CommandLineRunner {

    private final RoleRepository repository;

    public RoleCommandLineRunner(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        Stream.of("USER", "ADMIN").forEach(name -> repository.save(new Role(name)));
    }
}
