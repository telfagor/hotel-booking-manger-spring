package com.bolun.hotel.controller;

import com.bolun.hotel.dto.OrderCreateEditDto;
import com.bolun.hotel.dto.OrderReadDto;
import com.bolun.hotel.dto.PageResponse;
import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.dto.filters.OrderFilter;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.exception.InsufficientFundsException;
import com.bolun.hotel.service.OrderService;
import com.bolun.hotel.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @ModelAttribute("orderStatuses")
    public List<OrderStatus> orderStatuses() {
        return List.of(OrderStatus.values());
    }

    @GetMapping
    public String findAll(Model model, OrderFilter filter, Pageable pageable) {
        Page<OrderReadDto> orders = orderService.findAll(filter, pageable);
        model.addAttribute("orders", PageResponse.of(orders));
        model.addAttribute("filter", filter);
        return "order/orders";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") UUID id, Model model) {
        return orderService.findById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    return "order/order";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/new")
    public String getCreateOrderPage(@RequestParam("id") UUID apartmentId, Model model, Principal principal) {
        Optional<UserReadDto> maybeUser = userService.findByEmail(principal.getName());
        if (maybeUser.isPresent() && maybeUser.get().userDetail() != null) {
            model.addAttribute("apartmentId", apartmentId);
            model.addAttribute("order", new OrderCreateEditDto(null, null, null, apartmentId));
            return "order/create-order";
        }

        return "redirect:/userDetails/new";
    }

    @PostMapping
    public String create(@ModelAttribute("order") @Valid OrderCreateEditDto order, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "order/create-order";
        }

        try {
            orderService.create(order);
        } catch (InsufficientFundsException e) {
            model.addAttribute("error", e.getMessage());
            return "order/create-order";
        }

        orderService.create(order);
        return "redirect:/apartments";
    }

    @GetMapping("/{id}/update")
    public String getUpdateOrderStatusPage(@PathVariable("id") UUID id, Model model) {
        return orderService.findById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    return "order/update-order";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("{id}/update-status")
    public String updateOrderStatus(@PathVariable("id") UUID id, @RequestParam("orderStatus") OrderStatus status) {
        return orderService.updateOrderStatus(id, status)
                .map(it -> "redirect:/orders")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("{id}/delete")
    public String delete(@PathVariable("id") UUID id) {
        if (!orderService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        orderService.delete(id);
        return "redirect:/orders";
    }
}
