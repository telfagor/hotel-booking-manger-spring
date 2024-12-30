package com.bolun.hotel.dto.filters;

import com.bolun.hotel.entity.enums.ApartmentType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ApartmentFilter(Integer rooms,
                              Integer seats,
                              Integer dailyCostFrom,
                              Integer dailyCostTo,
                              ApartmentType apartmentType,
                              @DateTimeFormat(pattern = "dd.MM.yyyy")
                              LocalDate checkIn,
                              @DateTimeFormat(pattern = "dd.MM.yyyy")
                              LocalDate checkOut) {

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
}