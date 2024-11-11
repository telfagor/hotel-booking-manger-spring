package com.bolun.hotel.controller;

import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ModelAttribute("genders")
    private List<Gender> genders() {
        return Arrays.asList(Gender.values());
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/users";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") UUID id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", userService.findById(id).orElse(null));
                    return "user/user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/register")
    public String createUser(Model model, UserCreateEditDto user) {
        model.addAttribute("user", user);
        return "user/register";
    }

    @PostMapping
    public String create(@ModelAttribute("user") UserCreateEditDto user) {
        userService.create(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/update")
    public String updateUser(@PathVariable("id") UUID id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "user/update-user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") UUID id,
                       @ModelAttribute("user") UserCreateEditDto user) {
        return userService.update(id, user)
                .map(it -> "redirect:/users/{id}")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") UUID id) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/users";
    }
}
