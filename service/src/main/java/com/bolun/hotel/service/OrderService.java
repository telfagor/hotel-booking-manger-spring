package com.bolun.hotel.service;

import com.bolun.hotel.dto.OrderCreateEditDto;
import com.bolun.hotel.dto.OrderReadDto;
import com.bolun.hotel.dto.filters.OrderFilter;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.mapper.OrderCreateEditMapper;
import com.bolun.hotel.mapper.OrderReadMapper;
import com.bolun.hotel.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static com.bolun.hotel.entity.enums.OrderStatus.APPROVED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCreateEditMapper orderCreateEditMapper;
    private final OrderReadMapper orderReadMapper;
    private final OrderValidationService orderValidationService;

    @Transactional
    public OrderReadDto create(OrderCreateEditDto orderDto) {
        orderValidationService.validateUserOrder(orderDto);
        return Optional.of(orderDto)
                .map(orderCreateEditMapper::mapFrom)
                .map(orderRepository::save)
                .map(orderReadMapper::mapFrom)
                .orElseThrow();
    }

    @Transactional
    public Optional<OrderReadDto> updateOrderStatus(UUID id, OrderCreateEditDto orderDto) {
        orderValidationService.validateUserOrder(orderDto);
        return orderRepository.findActiveByIdWithLock(id)
                .map(order -> {
                    if (orderDto.orderStatus() == APPROVED) {
                        User user = order.getUser();
                        UserDetail userDetail = user.getUserDetail();
                        int totalCost = calculateTotalCost(order);
                        order.setTotalCost(totalCost);
                        setTotalCostToUser(userDetail, order);
                    }
                    order.setStatus(orderDto.orderStatus());
                    return orderRepository.saveAndFlush(order);
                })
                .map(orderReadMapper::mapFrom);
    }

    private int calculateTotalCost(Order order) {
        long reservationDays = ChronoUnit.DAYS.between(order.getCheckIn(), order.getCheckOut());
        return (int) reservationDays * order.getApartment().getDailyCost();
    }

    private void setTotalCostToUser(UserDetail userDetail, Order order) {
        Integer money = userDetail.getMoney();
        money -= order.getTotalCost();
        userDetail.setMoney(money);
    }

    public Page<OrderReadDto> findAll(UUID userId, OrderFilter filter, Pageable pageable) {
        return orderRepository.findAll(userId, filter, pageable)
                .map(orderReadMapper::mapFrom);
    }

    public Optional<OrderReadDto> findById(UUID id) {
        return orderRepository.findActiveById(id)
                .map(orderReadMapper::mapFrom);
    }

    public UUID findUserIdByOrderId(UUID orderId) {
        return orderRepository.findUserIdByOrderId(orderId)
                .orElseThrow();
    }

    @Transactional
    public boolean delete(UUID id) {
        return orderRepository.findActiveByIdWithLock(id)
                .map(order -> {
                    order.setDeleted(true);
                    orderRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}

