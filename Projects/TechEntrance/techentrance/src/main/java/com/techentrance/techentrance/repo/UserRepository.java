package com.techentrance.techentrance.repo;

import com.techentrance.techentrance.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
    User findBySessionId(UUID sessionId);

    User findById(UUID id);
}
