package com.bolun.hotel.util;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.enums.OrderStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class OrderFilter {

    LocalDate checkIn;
    LocalDate checkOut;
    Integer totalCost;
    OrderStatus status;
    User user;
    Apartment apartment;
}
