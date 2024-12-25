package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.OrderCreateEditDto;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.exception.InsufficientFundsException;
import com.bolun.hotel.exception.InvalidAgeException;
import com.bolun.hotel.repository.ApartmentRepository;
import com.bolun.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.bolun.hotel.entity.enums.OrderStatus.IN_PROGRESS;

@Component
@RequiredArgsConstructor
public class OrderCreateEditMapper implements Mapper<OrderCreateEditDto, Order> {

    private static final int ADULT_AGE = 18;
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
            UserDetail userDetail = user.getUserDetail();
            checkAge(userDetail);
            checkTotalCost(order, userDetail);
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

    private void checkAge(UserDetail userDetail) {
        int age = (int) ChronoUnit.YEARS.between(userDetail.getBirthdate(), LocalDate.now());
        if (age < ADULT_AGE) {
            throw new InvalidAgeException("Invalid age. You are minor.");
        }
    }

    private void checkTotalCost(Order order, UserDetail userDetail) {
        Integer totalCost = calculateTotalCost(order);
        if (userDetail.getMoney() < totalCost) {
            throw new InsufficientFundsException("Insufficient funds");
        }
    }

    private int calculateTotalCost(Order order) {
        long reservationDays = ChronoUnit.DAYS.between(order.getCheckIn(), order.getCheckOut());
        return (int) reservationDays * order.getApartment().getDailyCost();
    }
}
