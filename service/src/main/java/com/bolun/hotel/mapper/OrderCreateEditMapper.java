package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.OrderCreateEditDto;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.repository.ApartmentRepository;
import com.bolun.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.bolun.hotel.entity.enums.OrderStatus.IN_PROGRESS;

@Component
@RequiredArgsConstructor
public class OrderCreateEditMapper implements Mapper<OrderCreateEditDto, Order> {

    private final UserRepository userRepository;
    private final ApartmentRepository apartmentRepository;

    @Override
    public Order mapFrom(OrderCreateEditDto orderDto) {
        Order order = new Order();
        copy(orderDto, order);
        return order;
    }

    @Override
    public Order mapFrom(OrderCreateEditDto fromObject, Order toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(OrderCreateEditDto orderDto, Order order) {
        order.setCheckIn(orderDto.checkIn());
        order.setCheckOut(orderDto.checkOut());
        order.setStatus(IN_PROGRESS);

        Optional<User> maybeUser = getCurrentAuthenticatedUser();
        apartmentRepository.findById(orderDto.apartmentId())
                .ifPresent(order::setApartment);

        maybeUser.ifPresent(user -> {
            int totalCost = calculateTotalCost(order);
            order.setTotalCost(totalCost);
            order.add(user);
        });
    }

    private Optional<User> getCurrentAuthenticatedUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> (UserDetails) authentication.getPrincipal())
                .map(UserDetails::getUsername)
                .flatMap(userRepository::findByEmail);
    }

    private int calculateTotalCost(Order order) {
        long reservationDays = ChronoUnit.DAYS.between(order.getCheckIn(), order.getCheckOut());
        return (int) reservationDays * order.getApartment().getDailyCost();
    }
}
