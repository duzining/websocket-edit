package com.du.websocketedit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CustomController {

    @RequestMapping("/custom")
    public String html(){
        return "custom";
    }

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
}
