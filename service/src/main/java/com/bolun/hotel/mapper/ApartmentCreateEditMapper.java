package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.ApartmentCreateEditDto;
import com.bolun.hotel.entity.Apartment;
import org.springframework.stereotype.Component;

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
        apartment.setRooms(apartmentDto.rooms());
        apartment.setSeats(apartmentDto.seats());
        apartment.setDailyCost(apartmentDto.dailyCost());
        apartment.setApartmentType(apartmentDto.apartmentType());

        if (apartmentDto.photo() != null && !apartmentDto.photo().isEmpty()) {
            apartment.setPhoto(apartmentDto.photo().getOriginalFilename());
        }
    }
}
