package com.bolun.hotel.dto;

import com.bolun.hotel.entity.enums.OrderStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record OrderReadDto(UUID id,
                           LocalDate checkIn,
                           LocalDate checkOut,
                           Integer totalCost,
                           OrderStatus status,
                           UserReadDto user,
                           ApartmentReadDto apartment,
                           Instant createdAt,
                           Instant modifiedAt,
                           String createdBy,
                           String modifiedBy) {

}
