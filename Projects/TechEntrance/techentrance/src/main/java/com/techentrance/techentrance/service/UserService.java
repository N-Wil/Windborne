package com.techentrance.techentrance.service;


import com.techentrance.techentrance.model.User;
import com.techentrance.techentrance.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserService {
    private final UserRepository userRepository;
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User getUserBySessionId(UUID sessionId) {
        return userRepository.findBySessionId(sessionId);
    }
    public User getUserById(UUID id) {
        return userRepository.findById(id);
    }
}
