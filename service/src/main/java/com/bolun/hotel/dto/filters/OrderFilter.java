package com.bolun.hotel.dto.filters;

import com.bolun.hotel.entity.enums.OrderStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public record OrderFilter(@DateTimeFormat(pattern = "dd.MM.yyyy")
                          LocalDate checkIn,

                          @DateTimeFormat(pattern = "dd.MM.yyyy")
                          LocalDate checkOut,

                          @Min(value = 10, message = "At least $10")
                          Integer totalCost,

                          @Email
                          String email,
                          OrderStatus status,

                          @Min(value = 1, message = "At least 1")
                          Integer apartmentNumber) {
}
