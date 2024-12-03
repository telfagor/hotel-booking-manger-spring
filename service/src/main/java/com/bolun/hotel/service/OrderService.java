package com.bolun.hotel.service;

import com.bolun.hotel.dto.OrderCreateEditDto;
import com.bolun.hotel.dto.OrderReadDto;
import com.bolun.hotel.dto.filters.OrderFilter;
import com.bolun.hotel.entity.enums.OrderStatus;
import com.bolun.hotel.mapper.OrderCreateEditMapper;
import com.bolun.hotel.mapper.OrderReadMapper;
import com.bolun.hotel.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderCreateEditMapper orderCreateEditMapper;
    private final OrderReadMapper orderReadMapper;

    @Transactional
    public OrderReadDto create(OrderCreateEditDto orderDto) {
        return Optional.of(orderDto)
                .map(orderCreateEditMapper::mapFrom)
                .map(orderRepository::save)
                .map(orderReadMapper::mapFrom)
                .orElseThrow();
    }

    @Transactional
    public Optional<OrderReadDto> updateOrderStatus(UUID id, OrderStatus orderStatus) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(orderStatus);
                    return orderRepository.saveAndFlush(order);
                })
                .map(orderReadMapper::mapFrom);
    }

    public Page<OrderReadDto> findAll(OrderFilter filter, Pageable pageable) {
        return orderRepository.findAll(filter, pageable)
                .map(orderReadMapper::mapFrom);
    }

    public Optional<OrderReadDto> findById(UUID id) {
        return orderRepository.findById(id)
                .map(orderReadMapper::mapFrom);
    }

    @Transactional
    public boolean delete(UUID id) {
        return orderRepository.findById(id)
                .map(order -> {
                    orderRepository.delete(order);
                    orderRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    public List<OrderReadDto> findOrdersByUserId(UUID id) {
        return orderRepository.findAllByUserId(id).stream()
                .map(orderReadMapper::mapFrom)
                .toList();
    }
}

