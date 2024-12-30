package com.bolun.hotel.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

@UtilityClass
public class AppConstantsUtil {

    @Getter
    private final List<Map<String, String>> orderSortOptions = List.of(
            Map.of("value", "checkIn,asc", "text", "Check In (Ascending)"),
            Map.of("value", "checkIn,desc", "text", "Check In (Descending)"),
            Map.of("value", "checkOut,asc", "text", "Check Out (Ascending)"),
            Map.of("value", "checkOut,desc", "text", "Check Out (Descending)"),
            Map.of("value", "totalCost,asc", "text", "Total Cost (Low to High)"),
            Map.of("value", "totalCost,desc", "text", "Total Cost (High to Low)"),
            Map.of("value", "email,asc", "text", "Email (A-Z)"),
            Map.of("value", "email,desc", "text", "Email (Z-A)")
    );

    @Getter
    private final List<Map<String, String>> apartmentSortOptions = List.of(
            Map.of("value", "dailyCost,asc", "text", "Daily Cost (Low to High)"),
            Map.of("value", "dailyCost,desc", "text", "Daily Cost (High to Low)"),
            Map.of("value", "seats,asc", "text", "Seats (Low to High)"),
            Map.of("value", "seats,desc", "text", "Seats (High to Low)"),
            Map.of("value", "rooms,asc", "text", "Rooms (Low to High)"),
            Map.of("value", "rooms,desc", "text", "Rooms (High to Low)")
    );

    @Getter
    private final List<Map<String, String>> userSortOptions = List.of(
            Map.of("value", "firstName,asc", "text", "First Name (A-Z)"),
            Map.of("value", "firstName,desc", "text", "First Name (Z-A)"),
            Map.of("value", "lastName,asc", "text", "Last Name (A-Z)"),
            Map.of("value", "lastName,desc", "text", "Last Name (Z-A)")
    );
}
