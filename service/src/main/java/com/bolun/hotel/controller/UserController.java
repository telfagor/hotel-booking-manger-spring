package com.bolun.hotel.controller;

import com.bolun.hotel.dto.PageResponse;
import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.service.UserService;
import com.bolun.hotel.validation.group.CreateAction;
import com.bolun.hotel.validation.group.UpdateAction;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
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
    public String findAll(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "20") int size,
                          Model model) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<UserReadDto> userPage = userService.findAll(pageable);
        model.addAttribute("users", PageResponse.of(userPage));
        return "user/users";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") UUID id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "user/user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    public String getCreateUserPage(@ModelAttribute("user") UserCreateEditDto user) {
        return "user/registration";
    }

    @PostMapping
    public String create(/*@ModelAttribute("user")*/
                          @Validated({Default.class, CreateAction.class}) UserCreateEditDto user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/registration";
        }
        userService.create(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/update")
    public String getUpdateUserPage(@PathVariable("id") UUID id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "user/update-user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") UUID id,
                         @Validated({Default.class, UpdateAction.class}) UserCreateEditDto user,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "user/update-user";
        }

        return userService.update(id, user)
                .map(it -> "redirect:/users")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") UUID id) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/users";
    }
}
