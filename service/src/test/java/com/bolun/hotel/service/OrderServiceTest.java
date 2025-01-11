package com.bolun.hotel.service;

import com.bolun.hotel.dto.OrderCreateEditDto;
import com.bolun.hotel.dto.OrderReadDto;
import com.bolun.hotel.dto.filters.OrderFilter;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.exception.ApartmentNotFoundException;
import com.bolun.hotel.exception.InsufficientFundsException;
import com.bolun.hotel.exception.MinorAgeException;
import com.bolun.hotel.exception.UserNotFoundException;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.mapper.OrderCreateEditMapper;
import com.bolun.hotel.mapper.OrderReadMapper;
import com.bolun.hotel.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderCreateEditMapper orderCreateEditMapper;

    @Mock
    private OrderReadMapper orderReadMapper;

    @Mock
    private OrderValidationService orderValidationService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void create() {
        Order order = new Order();
        OrderCreateEditDto orderCreateEditDto = TestObjectsUtils.getOrderCreateEditDto();
        OrderReadDto orderReadDto = TestObjectsUtils.getOrderReadDto();
        doNothing().when(orderValidationService).validateUserOrder(orderCreateEditDto);
        doReturn(order).when(orderCreateEditMapper).mapFrom(orderCreateEditDto);
        doReturn(order).when(orderRepository).save(order);
        doReturn(orderReadDto).when(orderReadMapper).mapFrom(order);

        OrderReadDto actualResult = orderService.create(orderCreateEditDto);

        assertThat(actualResult).isEqualTo(orderReadDto);
        verify(orderValidationService).validateUserOrder(orderCreateEditDto);
        verify(orderCreateEditMapper).mapFrom(orderCreateEditDto);
        verify(orderRepository).save(order);
        verify(orderReadMapper).mapFrom(order);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenCreate() {
        OrderCreateEditDto orderCreateEditDto = TestObjectsUtils.getOrderCreateEditDto();
        doThrow(new UserNotFoundException("User not found")).when(orderValidationService).validateUserOrder(orderCreateEditDto);

        UserNotFoundException userNotFoundException =
                assertThrows(UserNotFoundException.class,
                        () -> orderValidationService.validateUserOrder(orderCreateEditDto));

        assertThat(userNotFoundException.getMessage()).isEqualTo("User not found");
        verify(orderValidationService).validateUserOrder(orderCreateEditDto);
        verifyNoInteractions(orderCreateEditMapper);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(orderReadMapper);
    }

    @Test
    void shouldThrowApartmentNotFoundExceptionWhenCreate() {
        OrderCreateEditDto orderCreateEditDto = TestObjectsUtils.getOrderCreateEditDto();
        doThrow(new ApartmentNotFoundException("Apartment not found")).when(orderValidationService).validateUserOrder(orderCreateEditDto);

        ApartmentNotFoundException apartmentNotFoundException =
                assertThrows(ApartmentNotFoundException.class, () -> orderValidationService.validateUserOrder(orderCreateEditDto));

        assertThat(apartmentNotFoundException.getMessage()).isEqualTo("Apartment not found");
        verify(orderValidationService).validateUserOrder(orderCreateEditDto);
        verifyNoInteractions(orderCreateEditMapper);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(orderReadMapper);
    }

    @Test
    void shouldThrowMinorAgeExceptionWhenCreate() {
        OrderCreateEditDto orderCreateEditDto = TestObjectsUtils.getOrderCreateEditDto();
        doThrow(new MinorAgeException("You are minor")).when(orderValidationService).validateUserOrder(orderCreateEditDto);

        MinorAgeException minorAgeException =
                assertThrows(MinorAgeException.class, () -> orderValidationService.validateUserOrder(orderCreateEditDto));

        assertThat(minorAgeException.getMessage()).isEqualTo("You are minor");
        verify(orderValidationService).validateUserOrder(orderCreateEditDto);
        verifyNoInteractions(orderCreateEditMapper);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(orderReadMapper);
    }

    @Test
    void shouldThrowInsufficientFundsExceptionWhenCreate() {
        OrderCreateEditDto orderCreateEditDto = TestObjectsUtils.getOrderCreateEditDto();
        doThrow(new InsufficientFundsException("Insufficient funds")).when(orderValidationService).validateUserOrder(orderCreateEditDto);

        InsufficientFundsException insufficientFundsException =
                assertThrows(InsufficientFundsException.class, () -> orderValidationService.validateUserOrder(orderCreateEditDto));

        assertThat(insufficientFundsException.getMessage()).isEqualTo("Insufficient funds");
        verify(orderValidationService).validateUserOrder(orderCreateEditDto);
        verifyNoInteractions(orderCreateEditMapper);
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(orderReadMapper);
    }

    @Test
    void shouldThrowAnExceptionWhenCreate() {
        Order order = new Order();
        OrderCreateEditDto orderCreateEditDto = TestObjectsUtils.getOrderCreateEditDto();
        doNothing().when(orderValidationService).validateUserOrder(orderCreateEditDto);
        doReturn(null).when(orderCreateEditMapper).mapFrom(orderCreateEditDto);

        assertThrows(NoSuchElementException.class, () -> orderService.create(orderCreateEditDto));

        verify(orderValidationService).validateUserOrder(orderCreateEditDto);
        verify(orderCreateEditMapper).mapFrom(orderCreateEditDto);
        verify(orderRepository, never()).save(order);
        verify(orderReadMapper, never()).mapFrom(order);
    }

    @Test
    void update() {
        Order order = new Order();
        OrderCreateEditDto orderCreateEditDto = TestObjectsUtils.getOrderCreateEditDto();
        OrderReadDto orderReadDto = TestObjectsUtils.getOrderReadDto();
        doNothing().when(orderValidationService).validateUserOrder(orderCreateEditDto);
        doReturn(Optional.of(order)).when(orderRepository).findActiveByIdWithLock(order.getId());
        doReturn(order).when(orderRepository).saveAndFlush(order);
        doReturn(orderReadDto).when(orderReadMapper).mapFrom(order);

        Optional<OrderReadDto> actualResult = orderService.updateOrderStatus(order.getId(), orderCreateEditDto);

        assertThat(actualResult)
                .isPresent()
                .contains(orderReadDto);
        verify(orderValidationService).validateUserOrder(orderCreateEditDto);
        verify(orderRepository).findActiveByIdWithLock(order.getId());
        verify(orderRepository).saveAndFlush(order);
        verify(orderReadMapper).mapFrom(order);
    }

    @Test
    void shouldReturnAnEmptyOptionalWhenUpdateFails() {
        Order order = new Order();
        OrderCreateEditDto orderCreateEditDto = TestObjectsUtils.getOrderCreateEditDto();
        doNothing().when(orderValidationService).validateUserOrder(orderCreateEditDto);
        doReturn(Optional.empty()).when(orderRepository).findActiveByIdWithLock(order.getId());

        Optional<OrderReadDto> actualResult = orderService.updateOrderStatus(order.getId(), orderCreateEditDto);

        assertThat(actualResult).isEmpty();
        verify(orderValidationService).validateUserOrder(orderCreateEditDto);
        verify(orderRepository).findActiveByIdWithLock(order.getId());
        verify(orderRepository, never()).saveAndFlush(order);
        verifyNoInteractions(orderReadMapper);
    }

    @Test
    void findAll() {
        UUID userId = UUID.randomUUID();
        OrderFilter filter = TestObjectsUtils.getOrderFilter();
        Order order1 = TestObjectsUtils.getOrder();
        Order order2 = TestObjectsUtils.getOrder2();
        OrderReadDto orderReadDto1 = TestObjectsUtils.getOrderReadDto();
        OrderReadDto orderReadDto2 = TestObjectsUtils.getOrderReadDto2();
        List<Order> orders = List.of(order1, order2);
        List<OrderReadDto> orderReadDtos = List.of(orderReadDto1, orderReadDto2);
        Page<Order> orderPage = new PageImpl<>(orders, PageRequest.of(0, 2), orders.size());
        doReturn(orderPage).when(orderRepository).findAll(eq(userId), eq(filter), any(Pageable.class));
        doReturn(orderReadDto1).when(orderReadMapper).mapFrom(order1);
        doReturn(orderReadDto2).when(orderReadMapper).mapFrom(order2);

        Page<OrderReadDto> actualResult = orderService.findAll(userId, filter, PageRequest.of(0, 2));

        assertThat(actualResult.getContent()).containsExactlyInAnyOrderElementsOf(orderReadDtos);
        assertThat(actualResult.getTotalElements()).isEqualTo(orders.size());
        assertThat(actualResult.getNumber()).isZero();
        assertThat(actualResult.getSize()).isEqualTo(2);
        verify(orderRepository).findAll(eq(userId), eq(filter), any(Pageable.class));
        verify(orderReadMapper).mapFrom(order1);
        verify(orderReadMapper).mapFrom(order2);
    }

    @Test
    void findUserIdByOrderId() {
        UUID orderId = UUID.randomUUID();
        doReturn(Optional.of(orderId)).when(orderRepository).findUserIdByOrderId(orderId);

        UUID actualResult = orderService.findUserIdByOrderId(orderId);

        assertThat(actualResult).isEqualTo(orderId);
        verify(orderRepository).findUserIdByOrderId(orderId);
    }

    @Test
    void shouldThrowAnExceptionWhenFindUserIdByOrderId() {
        UUID orderId = UUID.randomUUID();
        doThrow(NoSuchElementException.class).when(orderRepository).findUserIdByOrderId(orderId);

        assertThrows(NoSuchElementException.class, () -> orderService.findUserIdByOrderId(orderId));

        verify(orderRepository).findUserIdByOrderId(orderId);

    }

    @Test
    void delete() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        doReturn(Optional.of(order)).when(orderRepository).findActiveByIdWithLock(orderId);
        doNothing().when(orderRepository).flush();

        boolean actualResult = orderService.delete(orderId);

        assertTrue(actualResult);
        verify(orderRepository).findActiveByIdWithLock(orderId);
        verify(orderRepository).flush();
    }

    @Test
    void shouldReturnFalseWhenDeleteFails() {
        UUID orderId = UUID.randomUUID();
        doReturn(Optional.empty()).when(orderRepository).findActiveByIdWithLock(orderId);

        boolean actualResult = orderService.delete(orderId);

        assertFalse(actualResult);
        verify(orderRepository).findActiveByIdWithLock(orderId);
    }
}