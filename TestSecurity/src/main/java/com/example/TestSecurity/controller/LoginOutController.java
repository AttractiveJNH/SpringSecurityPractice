package com.example.TestSecurity.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring6.context.SpringContextUtils;

@Controller
public class LoginOutController {
    @GetMapping("/login")
    public String loginP(){
        return "login";
    }

    // 로그아웃 컨트롤러
    @GetMapping("/logout")
    public String logOut(HttpServletRequest request, HttpServletResponse response) throws Exception{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

}
