package com.bolun.hotel.controller;

import com.bolun.hotel.dto.ChangePasswordDto;
import com.bolun.hotel.dto.OrderReadDto;
import com.bolun.hotel.dto.PageResponse;
import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.dto.filters.OrderFilter;
import com.bolun.hotel.dto.filters.UserFilter;
import com.bolun.hotel.entity.enums.Gender;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.mapper.UserToUserCreateEditDtoMapper;
import com.bolun.hotel.service.CustomUserDetails;
import com.bolun.hotel.service.OrderService;
import com.bolun.hotel.service.PasswordChangeResult;
import com.bolun.hotel.service.UserService;
import com.bolun.hotel.validation.UserValidator;
import com.bolun.hotel.validation.group.CreateAction;
import com.bolun.hotel.validation.group.UpdateAction;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.bolun.hotel.entity.enums.Role.ADMIN;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final List<Map<String, String>> USER_SORT_OPTIONS = List.of(
            Map.of("value", "firstName,asc", "text", "First Name (A-Z)"),
            Map.of("value", "firstName,desc", "text", "First Name (Z-A)"),
            Map.of("value", "lastName,asc", "text", "Last Name (A-Z)"),
            Map.of("value", "lastName,desc", "text", "Last Name (Z-A)")
    );

    private static final List<Map<String, String>> ORDER_SORT_OPTIONS = List.of(
            Map.of("value", "checkIn,asc", "text", "Check In (Ascending)"),
            Map.of("value", "checkIn,desc", "text", "Check In (Descending)"),
            Map.of("value", "checkOut,asc", "text", "Check Out (Ascending)"),
            Map.of("value", "checkOut,desc", "text", "Check Out (Descending)"),
            Map.of("value", "totalCost,asc", "text", "Total Cost (Low to High)"),
            Map.of("value", "totalCost,desc", "text", "Total Cost (High to Low)"),
            Map.of("value", "email,asc", "text", "Email (A-Z)"),
            Map.of("value", "email,desc", "text", "Email (Z-A)")
    );

    private final UserService userService;
    private final OrderService orderService;
    private final UserValidator userValidator;
    private final UserToUserCreateEditDtoMapper userToUserCreateEditDtoMapper;

    @ModelAttribute("genders")
    private List<Gender> genders() {
        return Arrays.asList(Gender.values());
    }


    @GetMapping
    public String findAll(Model model, UserFilter filter, Pageable pageable) {
        Page<UserReadDto> userPage = userService.findAll(filter, pageable);
        model.addAttribute("data", PageResponse.of(userPage));
        model.addAttribute("filter", filter);
        model.addAttribute("sortOptions", USER_SORT_OPTIONS);
        model.addAttribute("selectedSort", pageable.getSort().toString());
        model.addAttribute("baseUrl", "/users");
        return "user/users";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == principal.id")
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") UUID id, Model model, OrderFilter filter, Pageable pageable) {
        return userService.findById(id)
                .map(user -> {
                    Page<OrderReadDto> orderPage = orderService.findAll(id, filter, pageable);
                    model.addAttribute("user", user);
                    model.addAttribute("data", PageResponse.of(orderPage));
                    model.addAttribute("orderStatuses", OrderStatus.values());
                    model.addAttribute("sortOptions", ORDER_SORT_OPTIONS);
                    model.addAttribute("selectedSort", pageable.getSort().toString());
                    model.addAttribute("filter", filter);
                    model.addAttribute("baseUrl", "/users/" + id);
                    return "user/user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/registration")
    public String getCreateUserPage(@ModelAttribute("user") UserCreateEditDto user) {
        return "user/registration";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Validated({Default.class, CreateAction.class}) UserCreateEditDto user,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (bindingResult.hasErrors()) {
            return "user/registration";
        }

        userService.create(user);

        if (customUserDetails != null && customUserDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.equals(ADMIN))) {
            return "redirect:/users";
        }

        return "redirect:/login";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == principal.id")
    @GetMapping("/{id}/update")
    public String getUpdateUserPage(@PathVariable("id") UUID id, Model model) {
        return userService.findById(id, userToUserCreateEditDtoMapper)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "user/update-user";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @PostMapping("/{id}/update")
    public String update(@ModelAttribute("id") @PathVariable("id") UUID id,
                         @ModelAttribute("user") @Validated({Default.class, UpdateAction.class}) UserCreateEditDto user,
                         BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "user/update-user";
        }

        return userService.update(id, user)
                .map(it -> "redirect:/apartments")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == principal.id")
    @GetMapping("/{id}/change-password")
    public String getChangePasswordPage(@PathVariable("id") UUID id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("id", id);
                    model.addAttribute("changePasswordDto", new ChangePasswordDto("", "", ""));
                    return "user/change-password";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == principal.id")
    @PostMapping("/{id}/change-password")
    public String changePassword(@ModelAttribute("id") @PathVariable("id") UUID id,
                                 @ModelAttribute("changePasswordDto") @Valid ChangePasswordDto changePasswordDto,
                                 BindingResult bindingResult,
                                 Model model) {

        if (bindingResult.hasErrors()) {
            return "user/change-password";
        }

        PasswordChangeResult passwordChangeResult = userService.changePassword(id, changePasswordDto);
        return switch (passwordChangeResult) {
            case SUCCESS -> "redirect:/users/" + id;
            case INVALID_PASSWORD -> {
                model.addAttribute("error", "The current password is incorrect");
                yield "user/change-password";
            }
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        };
    }

    @PreAuthorize("hasAuthority('ADMIN') or #id == principal.id")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") UUID id, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (!userService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (customUserDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.equals(ADMIN))) {
            return "redirect:/users";
        }

        return "redirect:/login";
    }
}
