package com.bolun.hotel.controller;

import com.bolun.hotel.dto.UserDetailCreateEditDto;
import com.bolun.hotel.service.UserDetailService;
import com.bolun.hotel.validation.group.CreateAction;
import com.bolun.hotel.validation.group.UpdateAction;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

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

    @GetMapping("/{id}/update")
    public String getUpdateUserDetailPage(@PathVariable("id") UUID id, Model model) {
        return userDetailService.findByUserId(id)
                .map(userDetail -> {
                    model.addAttribute("userDetail", userDetail);
                    return "userDetail/update-user-detail";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@ModelAttribute("id") @PathVariable("id") UUID id,
                         @ModelAttribute("userDetail") @Validated({Default.class, UpdateAction.class}) UserDetailCreateEditDto userDetail,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "userDetail/update-user-detail";
        }

        userDetailService.update(id, userDetail)
                .map(it -> "redirect:/users/me")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return "redirect:/users/me";
    }
}
