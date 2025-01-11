package com.bolun.hotel.service;

import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.dto.OrderCreateEditDto;
import com.bolun.hotel.dto.UserDetailReadDto;
import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.exception.ApartmentNotFoundException;
import com.bolun.hotel.exception.InsufficientFundsException;
import com.bolun.hotel.exception.MinorAgeException;
import com.bolun.hotel.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Component
public class OrderValidationService {

    @Value("${order.adult.age}")
    private final int adultAge;
    private final UserService userService;
    private final ApartmentService apartmentService;


    public void validateUserOrder(OrderCreateEditDto order) {
        UserReadDto user = userService.findById(order.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        ApartmentReadDto apartment = apartmentService.findById(order.apartmentId())
                .orElseThrow(() -> new ApartmentNotFoundException("Apartment not found"));

        UserDetailReadDto userDetail = user.userDetail();
        checkAge(userDetail);
        int totalCost = calculateTotalCost(order, apartment);
        checkTotalCost(userDetail, totalCost);
    }

    private void checkAge(UserDetailReadDto userDetail) {
        int age = (int) ChronoUnit.YEARS.between(userDetail.birthdate(), LocalDate.now());
        if (age < adultAge) {
            throw new MinorAgeException("Invalid age. You are minor.");
        }
    }

    private void checkTotalCost(UserDetailReadDto userDetail, int totalCost) {
        if (userDetail.money() < totalCost) {
            throw new InsufficientFundsException("Insufficient funds");
        }
    }

    private int calculateTotalCost(OrderCreateEditDto order, ApartmentReadDto apartment) {
        long reservationDays = ChronoUnit.DAYS.between(order.getCheckIn(), order.getCheckOut());
        return (int) reservationDays * apartment.dailyCost();
    }
}
