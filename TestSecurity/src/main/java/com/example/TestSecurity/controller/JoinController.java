package com.example.TestSecurity.controller;

import com.example.TestSecurity.dto.JoinDto;
import com.example.TestSecurity.service.JoinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class JoinController {

    @Autowired
    private JoinService joinService;

    @GetMapping("/join")
    public String joinP(){
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(JoinDto joinDto){

        log.info(joinDto.getUsername());

        joinService.joinProcess(joinDto);
        return "redirect:/login";
    }
}
