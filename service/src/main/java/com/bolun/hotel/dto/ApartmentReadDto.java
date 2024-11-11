package com.bolun.hotel.dto;

import com.bolun.hotel.entity.enums.ApartmentType;

import java.util.UUID;

public record ApartmentReadDto(UUID id,
                               Integer roomNumber,
                               Integer seatNumber,
                               Integer dailyCost,
                               ApartmentType apartmentType,
                               String photo) {

}
