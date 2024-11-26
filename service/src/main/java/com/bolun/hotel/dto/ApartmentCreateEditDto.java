package com.bolun.hotel.dto;

import com.bolun.hotel.entity.enums.ApartmentType;
import com.bolun.hotel.validation.ValidPhoto;
import com.bolun.hotel.validation.group.CreateAction;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ApartmentCreateEditDto(
        @NotNull(message = "Rooms are required", groups = CreateAction.class)
        @Min(value = 1, message = "At least one room is required")
        @Max(value = 4, message = "At most 4 rooms")
        Integer rooms,

        @NotNull(message = "Seats are required", groups = CreateAction.class)
        @Min(value = 1, message = "At least one seat is required")
        @Max(value = 8, message = "At most 8 seats")
        Integer seats,

        @NotNull(message = "Daily cost is required", groups = CreateAction.class)
        @Min(value = 10, message = "At least $10 is required")
        @Max(value = 120, message = "At most $120")
        Integer dailyCost,

        ApartmentType apartmentType,

        @NotNull(message = "Photo is required", groups = CreateAction.class)
        @ValidPhoto(groups = CreateAction.class)
        MultipartFile photo) {
}

