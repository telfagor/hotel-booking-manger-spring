package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.ApartmentCreateEditDto;
import com.bolun.hotel.entity.Apartment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApartmentCreateEditMapper implements Mapper<ApartmentCreateEditDto, Apartment> {

    @Override
    public Apartment mapFrom(ApartmentCreateEditDto apartmentDto) {
        Apartment apartment = new Apartment();
        copy(apartmentDto, apartment);
        return apartment;
    }

    @Override
    public Apartment mapFrom(ApartmentCreateEditDto apartmentDto, Apartment apartment) {
        copy(apartmentDto, apartment);
        return apartment;
    }

    private void copy(ApartmentCreateEditDto apartmentDto, Apartment apartment) {
        apartment.setRoomNumber(apartmentDto.rooms());
        apartment.setSeatNumber(apartmentDto.seats());
        apartment.setDailyCost(apartmentDto.dailyCost());
        apartment.setApartmentType(apartmentDto.apartmentType());

        Optional.of(apartmentDto.photo())
                .ifPresent(photo -> apartment.setPhoto(photo.getOriginalFilename()));
    }
}
