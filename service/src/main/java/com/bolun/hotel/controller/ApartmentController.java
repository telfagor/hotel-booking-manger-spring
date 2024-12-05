package com.bolun.hotel.controller;

import com.bolun.hotel.dto.ApartmentCreateEditDto;
import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.dto.PageResponse;
import com.bolun.hotel.dto.filters.ApartmentFilter;
import com.bolun.hotel.entity.enums.ApartmentType;
import com.bolun.hotel.service.ApartmentService;
import com.bolun.hotel.validation.group.CreateAction;
import com.bolun.hotel.validation.group.UpdateAction;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;

    @ModelAttribute("apartmentTypes")
    public List<ApartmentType> apartmentTypes() {
        return List.of(ApartmentType.values());
    }

    @GetMapping
    public String findAll(ApartmentFilter filter, Pageable pageable, Model model) {
        Page<ApartmentReadDto> apartments = apartmentService.findAll(filter, pageable);
        model.addAttribute("apartments", PageResponse.of(apartments));
        model.addAttribute("filter", filter);
        return "apartment/apartments";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable UUID id, Model model) {
        return apartmentService.findById(id)
                .map(apartment -> {
                    model.addAttribute("apartment", apartment);
                    return "apartment/apartment";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/new")
    public String getCreateApartmentPage(@ModelAttribute("apartment") ApartmentCreateEditDto apartment) {
        return "apartment/create-apartment";
    }

    @PostMapping
    public String create(@ModelAttribute("apartment") @Validated({Default.class, CreateAction.class}) ApartmentCreateEditDto apartment,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "apartment/create-apartment";
        }
        apartmentService.create(apartment);
        return "redirect:/apartments";
    }

    @GetMapping("/{id}/update")
    public String getUpdateApartmentPage(@ModelAttribute("id") @PathVariable("id") UUID id, Model model) {
        return apartmentService.findById(id)
                .map(apartment -> {
                    model.addAttribute("apartment", apartment);
                    return "apartment/update-apartment";
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{id}/update")
    public String update(@ModelAttribute("id") @PathVariable("id") UUID id,
                         @ModelAttribute("apartment") @Validated({Default.class, UpdateAction.class}) ApartmentCreateEditDto apartment,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "apartment/update-apartment";
        }

        return apartmentService.update(id, apartment)
                .map(it -> "redirect:/apartments")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("{id}/delete")
    public String delete(@PathVariable("id") UUID id) {
        if (!apartmentService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/apartments";
    }
}
