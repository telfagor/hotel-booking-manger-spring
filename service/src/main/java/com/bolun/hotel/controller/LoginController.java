package com.bolun.hotel.controller;

import com.bolun.hotel.dto.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String loginPage(@ModelAttribute("login") LoginDto loginDto) {
        return "user/login";
    }
}
