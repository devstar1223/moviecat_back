package com.moviecat.www.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcViewController {
    @GetMapping("/success")
    String suc(Authentication auth){
        System.out.println(auth);
        return "success";
    }

    @GetMapping("/fail")
    String fail(){
        return "fail";
    }

    @GetMapping("/login")
    public String login(Authentication auth) {
        System.out.println(auth);
        return "login";
    }

}
