package com.bolun.hotel.integration.filter;

import com.bolun.hotel.entity.enums.ApartmentType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
class ApartmentFilter {

   Integer rooms;
   Integer seats;
   Integer dailyCost;
   ApartmentType type;
}
