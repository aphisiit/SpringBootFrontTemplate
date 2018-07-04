package com.training.frontend.service;

import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> findByFirstName(String firstName);
}
