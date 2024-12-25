package com.bolun.hotel.controller;

import com.bolun.hotel.dto.UserDetailCreateEditDto;
import com.bolun.hotel.service.UserDetailService;
import com.bolun.hotel.validation.group.CreateAction;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/userDetails")
@RequiredArgsConstructor
public class UserDetailController {

    private final UserDetailService userDetailService;

    @GetMapping("/new")
    public String getCreateUserDetailPage(@ModelAttribute("userDetail") UserDetailCreateEditDto userDetail) {
        return "userDetail/create-user-detail";
    }

    @PostMapping
    public String create(@ModelAttribute("userDetail") @Validated({Default.class, CreateAction.class}) UserDetailCreateEditDto userDetail,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "userDetail/create-user-detail";
        }

        userDetailService.create(userDetail);
        return "redirect:/apartments";
    }
}
