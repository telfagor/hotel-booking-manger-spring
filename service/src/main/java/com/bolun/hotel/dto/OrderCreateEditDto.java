package com.bolun.hotel.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

public record OrderCreateEditDto(@NotNull(message = "Check-in is required")
                                 @FutureOrPresent(message = "Check-in must be in the present or future")
                                 @DateTimeFormat(pattern = "dd.MM.yyyy")
                                 LocalDate checkIn,

                                 @NotNull(message = "Check-Out is required")
                                 @Future(message = "Check-out must be in the future")
                                 @DateTimeFormat(pattern = "dd.MM.yyyy")
                                 LocalDate checkOut,
                                 UUID userId,
                                 UUID apartmentId) {


}
