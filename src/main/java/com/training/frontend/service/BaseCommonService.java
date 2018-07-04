package com.training.frontend.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface BaseCommonService {

    public ResponseEntity<String> findByCriteria(Map<String, Object> objectMap);

    public ResponseEntity<String> findSize(Map<String, Object> objectMap);

    public ResponseEntity<String> save(Map parameter);

    public ResponseEntity<String> savePut(Long id, Map parameter);

    public ResponseEntity<String> delete(Long id);

    public ResponseEntity<String> get(Long id);

    public ResponseEntity<String> load();
}
