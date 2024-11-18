package com.bolun.hotel.dto;

import com.bolun.hotel.entity.enums.ApartmentType;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class ApartmentFilter {

   Integer rooms;
   Integer seats;
   Integer dailyCost;
   ApartmentType type;
   LocalDate checkIn;
   LocalDate checkOut;
}
