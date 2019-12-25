package com.du.websocketedit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller()
public class CustomController {

    @RequestMapping("/custom")
    public String html(){
        return "custom";
    }


}
