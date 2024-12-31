package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.ApartmentCreateEditDto;
import com.bolun.hotel.entity.Apartment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
        apartment.setRooms(apartmentDto.rooms());
        apartment.setSeats(apartmentDto.seats());
        apartment.setDailyCost(apartmentDto.dailyCost());
        apartment.setApartmentType(apartmentDto.apartmentType());

        Optional.ofNullable(apartmentDto.photo())
                .map(MultipartFile::getOriginalFilename)
                .filter(filename -> !filename.isEmpty())
                .ifPresent(apartment::setPhoto);
    }
}
