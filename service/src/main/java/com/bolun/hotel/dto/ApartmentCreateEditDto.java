package com.bolun.hotel.dto;

import com.bolun.hotel.entity.enums.ApartmentType;
import org.springframework.web.multipart.MultipartFile;

public record ApartmentCreateEditDto (
        Integer rooms,
        Integer seats,
        Integer dailyCost,
        ApartmentType apartmentType,
        MultipartFile photo) {

}
