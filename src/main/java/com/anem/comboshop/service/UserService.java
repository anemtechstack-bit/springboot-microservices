package com.anem.comboshop.service;

import com.anem.comboshop.domain.AppUser;
import com.anem.comboshop.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder){
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public AppUser registerUser(String username, String email, String rawPassword){
        if(userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already exists");
        }
        AppUser u = new AppUser();
        u.setUsername(username.trim());
        u.setEmail(email==null? null : email.trim());
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setRoles(Set.of("ROLE_USER"));
        return userRepository.save(u);
    }

    @Transactional
    public AppUser ensureAdminSeed(){
        return userRepository.findByUsername("admin").orElseGet(() -> {
            AppUser a = new AppUser();
            a.setUsername("admin");
            a.setEmail("admin@example.com");
            a.setPasswordHash(encoder.encode("admin"));
            a.setRoles(Set.of("ROLE_ADMIN"));
            return userRepository.save(a);
        });
    }
}
