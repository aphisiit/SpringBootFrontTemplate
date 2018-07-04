package com.training.frontend.general;

import com.training.frontend.service.BaseCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseCommonController {

    static final Logger LOGGER = LoggerFactory.getLogger(BaseCommonController.class);

    public BaseCommonService service;

    public abstract void setService(BaseCommonService service);


    @GetMapping(value="/findSize",produces="application/json")
    @ResponseBody
    public ResponseEntity<String> findItemSize(HttpServletRequest request) {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("queryString", request.getQueryString());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        ResponseEntity<String>  responseEntity =  service.findSize(objectMap);
        return new ResponseEntity<String>(responseEntity.getBody(), headers, HttpStatus.OK);
    }


    @GetMapping(value="/findByCriteria",produces="application/json", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String>  findByCriteria(HttpServletRequest request) {
        Map<String, Object> objectMap = new HashMap<String, Object>();
        objectMap.put("queryString", request.getQueryString());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        ResponseEntity<String>  responseEntity =  service.findByCriteria(objectMap);
        return new ResponseEntity<String>(responseEntity.getBody(), headers, HttpStatus.OK);

    }

    @PostMapping(produces = "text/html;charset=utf-8", headers = "Accept=application/json; charset=utf-8")
    public ResponseEntity save(HttpServletRequest request,@RequestParam("id") Long id) {
        LOGGER.info("id="+id);
        ResponseEntity<String> returnResult = null;
        Map<String,String[]> mapClone = new HashMap<String,String[]>(request.getParameterMap());
        if(id != null){
            returnResult = service.savePut(id, mapClone);
        }else{
            returnResult = service.save(mapClone);
        }
        return new ResponseEntity<String>(returnResult.getBody(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {

        if (null == service.delete(id)) {
            return new ResponseEntity("No User found for ID " + id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(id, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        ResponseEntity<String>  responseEntity =  service.get(id);
        return new ResponseEntity<String>(responseEntity.getBody(), headers, HttpStatus.OK);
    }



    @GetMapping(value="",produces="application/json", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String>  load(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        ResponseEntity<String>  responseEntity =  service.load();
        return new ResponseEntity<String>(responseEntity.getBody(), headers, HttpStatus.OK);

    }
}
