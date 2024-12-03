package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.entity.Apartment;
import org.springframework.stereotype.Component;

@Component
public class ApartmentReadMapper implements Mapper<Apartment, ApartmentReadDto> {

    @Override
    public ApartmentReadDto mapFrom(Apartment apartment) {
        return new ApartmentReadDto(
                apartment.getId(),
                apartment.getApartmentNumber(),
                apartment.getRoomNumber(),
                apartment.getSeatNumber(),
                apartment.getDailyCost(),
                apartment.getApartmentType(),
                apartment.getPhoto()
        );
    }
}
