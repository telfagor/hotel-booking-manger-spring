package com.bolun.hotel.controller;

import com.bolun.hotel.dto.ApartmentCreateEditDto;
import com.bolun.hotel.dto.ApartmentFilter;
import com.bolun.hotel.entity.enums.ApartmentType;
import com.bolun.hotel.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
    public String findAll(Model model, ApartmentFilter filter, Pageable pageable) {
        model.addAttribute("apartments", apartmentService.findAll(filter, pageable));
        return "apartment/apartments";
    }

    @GetMapping("/new")
    public String getCreateApartmentPage(@ModelAttribute("apartment") ApartmentCreateEditDto apartment) {
        return "apartment/create-apartment";
    }

    @PostMapping
    public String create(ApartmentCreateEditDto apartment) {
        apartmentService.create(apartment);
        return "redirect:/apartments";
    }
}
