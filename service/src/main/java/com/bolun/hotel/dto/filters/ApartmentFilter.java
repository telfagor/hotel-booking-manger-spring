package com.bolun.hotel.dto.filters;

import com.bolun.hotel.entity.enums.ApartmentType;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Value
public class ApartmentFilter {

    public ApartmentFilter(Integer rooms,
                           Integer seats,
                           Integer dailyCostFrom,
                           Integer dailyCostTo,
                           ApartmentType apartmentType,
                           @DateTimeFormat(pattern = "dd.MM.yyyy")
                           LocalDate checkIn,
                           @DateTimeFormat(pattern = "dd.MM.yyyy")
                           LocalDate checkOut) {
        this.rooms = rooms;
        this.seats = seats;
        this.dailyCostFrom = dailyCostFrom;
        this.dailyCostTo = dailyCostTo;
        this.apartmentType = apartmentType;
        this.checkIn = checkIn == null ? LocalDate.now() : checkIn;
        this.checkOut = checkOut == null ? LocalDate.now().plusDays(1) : checkOut;
    }

    Integer rooms;
    Integer seats;
    Integer dailyCostFrom;
    Integer dailyCostTo;
    ApartmentType apartmentType;

    @NotNull(message = "check-in is required")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    LocalDate checkIn;

    @NotNull(message = "check-out is required")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    LocalDate checkOut;
}
