package com.training.frontend.service.impl;

import com.training.frontend.service.AbstractBackendService;
import com.training.frontend.service.BaseCommonService;
import com.training.frontend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

@Service("UserService")
public class UserServiceImpl extends AbstractBackendService implements UserService,BaseCommonService {

//    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Override
    public void setRestTemplate(RestTemplate restTemplate) {
        super.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> findByFirstName(String firstName) {
        String url = "/userCustom/findByFirstNameIgnoreCaseContaining?firstName=" + firstName;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Content-Type", "application/json; charset=utf-8");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> entity1 =  getResultString(url,entity);
        return  entity1;
    }

    @Override
    public ResponseEntity<String> findByCriteria(Map<String, Object> objectMap) {
        return null;
    }

    @Override
    public ResponseEntity<String> findSize(Map<String, Object> objectMap) {
        return null;
    }

    @Override
    public ResponseEntity<String> save(Map parameter) {
        return null;
    }

    @Override
    public ResponseEntity<String> savePut(Long id, Map parameter) {
        return null;
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        String url = "/userCustom/deleteById/" + id;
        ResponseEntity entity = deleteSend(HttpMethod.DELETE,url);
        return entity;
    }

    @Override
    public ResponseEntity<String> get(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<String> load() {

        String url = "/userCustom/findAll";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Content-Type", "application/json; charset=utf-8");
        HttpEntity<String> entity = new HttpEntity<String>("", headers);

        ResponseEntity<String> entity1 =  getResultString(url,entity);
        return  entity1;
    }

    @Override
    public ResponseEntity<String> findById(Long id) {
        String url = "/userCustom/" + id;
        ResponseEntity entity = postWithJson(null,HttpMethod.POST,url);
        return entity;
    }
}
