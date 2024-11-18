package com.bolun.hotel.controller;

import com.bolun.hotel.dto.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String loginPage(@ModelAttribute("login") LoginDto loginDto) {
        return "user/login";
    }

    @PostMapping
    public String login(@ModelAttribute("login") @Validated LoginDto loginDto,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/login";
        }
        return "redirect:/users";
    }
}
