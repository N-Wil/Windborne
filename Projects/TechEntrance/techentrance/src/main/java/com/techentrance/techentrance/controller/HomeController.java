package com.techentrance.techentrance.controller;

import com.techentrance.techentrance.security.Security;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller @RequiredArgsConstructor
public class HomeController {
    private final Security security;

    @GetMapping("/")
    public String home(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = security.validateSession(request);
        if(cookie==null){
            return "redirect:/login";
        }
        response.addCookie(cookie);
        return "index";
    }
}
