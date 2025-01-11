package com.bolun.hotel.controller;

import com.bolun.hotel.dto.OrderCreateEditDto;
import com.bolun.hotel.dto.OrderReadDto;
import com.bolun.hotel.dto.PageResponse;
import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.dto.filters.OrderFilter;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.exception.InsufficientFundsException;
import com.bolun.hotel.exception.MinorAgeException;
import com.bolun.hotel.service.OrderService;
import com.bolun.hotel.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.bolun.hotel.entity.enums.OrderStatus.IN_PROGRESS;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

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

    private final OrderService orderService;
    private final UserService userService;

    @ModelAttribute("orderStatuses")
    public List<OrderStatus> orderStatuses() {
        return List.of(OrderStatus.values());
    }

    @GetMapping
    public String findAll(Model model, OrderFilter filter, Pageable pageable) {
        Page<OrderReadDto> orders = orderService.findAll(filter.userId(), filter, pageable);
        model.addAttribute("data", PageResponse.of(orders));
        model.addAttribute("filter", filter);
        model.addAttribute("sortOptions", ORDER_SORT_OPTIONS);
        model.addAttribute("selectedSort", pageable.getSort().toString());
        model.addAttribute("baseUrl", "/orders");
        return "order/orders";
    }

    @PreAuthorize("hasAuthority('ADMIN') or @orderService.findUserIdByOrderId(#id) == principal.id")
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") UUID id,
                           @RequestHeader(value = "Referer", required = false) String referer,
                           Model model) {
        return orderService.findById(id)
                .map(order -> {
                    model.addAttribute("order", order);
                    model.addAttribute("referer", referer != null ? referer : "/apartments");
                    return "order/order";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/new")
    public String getCreateOrderPage(@RequestParam("id") UUID apartmentId, Model model, Principal principal) {
        Optional<UserReadDto> maybeUser = userService.findByEmail(principal.getName());
        if (maybeUser.isPresent() && maybeUser.get().userDetail() != null) {
            model.addAttribute("apartmentId", apartmentId);
            model.addAttribute("order", new OrderCreateEditDto(null, null,
                    IN_PROGRESS, maybeUser.get().id(), apartmentId));
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
        } catch (InsufficientFundsException | MinorAgeException e) {
            model.addAttribute("error", e.getMessage());
            return "order/create-order";
        }

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
    public String updateOrderStatus(@PathVariable("id") UUID id, OrderCreateEditDto order) {
        return orderService.updateOrderStatus(id, order)
                .map(it -> "redirect:/orders")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("{id}/delete")
    public String delete(@PathVariable("id") UUID id) {
        if (!orderService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/orders";
    }
}
