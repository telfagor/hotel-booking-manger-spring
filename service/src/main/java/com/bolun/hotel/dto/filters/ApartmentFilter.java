package com.bolun.hotel.dto.filters;

import com.bolun.hotel.entity.enums.ApartmentType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Value
@Builder
public class ApartmentFilter {

    public ApartmentFilter(Integer rooms,
                           Integer seats,
                           Integer dailyCost,
                           ApartmentType apartmentType,
                           LocalDate checkIn,
                           LocalDate checkOut) {
        this.rooms = rooms;
        this.seats = seats;
        this.dailyCost = dailyCost;
        this.apartmentType = apartmentType;
        this.checkIn = checkIn == null ? LocalDate.now() : checkIn;
        this.checkOut = checkOut == null ? LocalDate.now().plusDays(1) : checkOut;
    }

    Integer rooms;
    Integer seats;
    Integer dailyCost;
    ApartmentType apartmentType;

    @NotNull(message = "check-in is required")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    LocalDate checkIn;

    @NotNull(message = "check-out is required")
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    LocalDate checkOut;
}
