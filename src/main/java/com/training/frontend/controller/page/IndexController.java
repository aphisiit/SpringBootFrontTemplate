package com.training.frontend.controller.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/")
    public String indexPage(){
        LOGGER.info("This is controller of index page.");
        return "index";
    }
}
