package com.spring_security.services;

import com.spring_security.entities.User;
import com.spring_security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserInitialization {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @EventListener(classes = ContextRefreshedEvent.class)
    public void initializeUser() {
        if (userRepository.count() == 0) {
            var user = User.builder()
                    .name("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin@123"))
                    .role(User.ROLE.ADMIN)
                    .isActive(true)
                    .build();
            userRepository.save(user);
        }
    }
}
