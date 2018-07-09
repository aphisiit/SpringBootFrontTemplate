package com.training.frontend.controller.app;

import com.training.frontend.general.BaseCommonController;
import com.training.frontend.service.BaseCommonService;
import com.training.frontend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/users")
public class UserController extends BaseCommonController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource(name="UserService")
    @Override
    public void setService(BaseCommonService service) {
        super.service = service;
    }

    @GetMapping(value = "/findAllUsers",headers = "Accept=application/json; charset=UTF-8;")
    public ResponseEntity<String> getAllUser(){
        LOGGER.info("getAllUser");
        return service.load();
    }

    @GetMapping(value = "/findByFirstName",headers = "Accept=application/json; charset=UTF-8")
    public ResponseEntity<String> findByFirstName(@RequestParam("firstName") String firstName){
        LOGGER.info("firstName : {}",firstName);
        return ((UserService) service).findByFirstName(firstName);
    }

    @PostMapping(value = "/deleteById/{id}",headers = "Accept=application/json; charset=UTF-8")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id){
        LOGGER.info("id : {}",id);
        return service.delete(id);
    }

    @PostMapping(value = "/findById/{id}",headers = "Accept=application/json; charset=UTF-8")
    public ResponseEntity<String> findById(@PathVariable("id") Long id){
        LOGGER.info("id : {}",id);
        return ((UserService) service).findById(id);
    }
}
