package com.bolun.hotel.dto;

import com.bolun.hotel.validation.DataRange;
import com.bolun.hotel.validation.DataRangeValidator;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;


@DataRange
public record OrderCreateEditDto(@NotNull(message = "Check-in is required")
                                 @FutureOrPresent(message = "Check-in must be in the present or future")
                                 @DateTimeFormat(pattern = "dd.MM.yyyy")
                                 LocalDate checkIn,
                                 @NotNull(message = "Check-Out is required")
                                 @Future(message = "Check-out must be in the future")
                                 @DateTimeFormat(pattern = "dd.MM.yyyy")
                                 LocalDate checkOut,
                                 UUID userId,
                                 UUID apartmentId) implements DataRangeValidator {

    public OrderCreateEditDto(LocalDate checkIn,
                              LocalDate checkOut,
                              UUID userId,
                              UUID apartmentId) {
        this.checkIn = checkIn != null ? checkIn : LocalDate.now();
        this.checkOut = checkOut;
        this.userId = userId;
        this.apartmentId = apartmentId;
    }

    @Override
    public LocalDate getCheckIn() {
        return checkIn;
    }

    @Override
    public LocalDate getCheckOut() {
        return checkOut;
    }
}
