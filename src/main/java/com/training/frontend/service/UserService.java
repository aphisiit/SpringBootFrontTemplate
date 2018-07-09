package com.training.frontend.service;

import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> findByFirstName(String firstName);
    ResponseEntity<String> findById(Long id);
}
