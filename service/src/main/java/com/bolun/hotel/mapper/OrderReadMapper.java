package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.dto.OrderReadDto;
import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.repository.ApartmentRepository;
import com.bolun.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderReadMapper implements Mapper<Order, OrderReadDto> {

    private final UserRepository userRepository;
    private final ApartmentRepository apartmentRepository;
    private final UserReadMapper userReadMapper;
    private final ApartmentReadMapper apartmentReadMapper;

    @Override
    public OrderReadDto mapFrom(Order order) {
        UserReadDto user = findUser(order);
        ApartmentReadDto apartment = findApartment(order);

        return new OrderReadDto(
                order.getId(),
                order.getCheckIn(),
                order.getCheckOut(),
                order.getTotalCost(),
                order.getStatus(),
                user,
                apartment,
                order.getCreatedAt(),
                order.getModifiedAt(),
                order.getCreatedBy(),
                order.getModifiedBy()
        );
    }

    private UserReadDto findUser(Order order) {
        return userRepository.findById(order.getUser().getId())
                .map(userReadMapper::mapFrom)
                .orElse(null);
    }

    private ApartmentReadDto findApartment(Order order) {
        return apartmentRepository.findById(order.getApartment().getId())
                .map(apartmentReadMapper::mapFrom)
                .orElse(null);
    }
}
