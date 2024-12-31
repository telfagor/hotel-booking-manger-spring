package com.bolun.hotel.service;

import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.exception.InsufficientFundsException;
import com.bolun.hotel.exception.InvalidAgeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class OrderValidationService {

    @Value("${order.adult.age:18}")
    private int adultAge;

    public void validateUserOrder(User user, int totalCost) {
        UserDetail userDetail = user.getUserDetail();
        checkAge(userDetail);
        checkTotalCost(userDetail, totalCost);
    }

    private void checkAge(UserDetail userDetail) {
        int age = (int) ChronoUnit.YEARS.between(userDetail.getBirthdate(), LocalDate.now());
        if (age < adultAge) {
            throw new InvalidAgeException("Invalid age. You are minor.");
        }
    }

    private void checkTotalCost(UserDetail userDetail, int totalCost) {
        if (userDetail.getMoney() < totalCost) {
            throw new InsufficientFundsException("Insufficient funds");
        }
    }
}
