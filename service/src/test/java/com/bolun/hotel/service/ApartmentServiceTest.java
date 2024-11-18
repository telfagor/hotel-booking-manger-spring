package com.bolun.hotel.service;

import com.bolun.hotel.dto.ApartmentFilter;
import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.mapper.ApartmentReadMapper;
import com.bolun.hotel.repository.ApartmentRepository;
import com.querydsl.core.types.Predicate;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApartmentServiceTest {

    @Mock
    private ApartmentRepository apartmentRepository;

    @Mock
    private ApartmentReadMapper apartmentReadMapper;

    @InjectMocks
    private ApartmentService apartmentService;

    @Test
    void findAll() {
        Apartment apartment1 = Apartment.builder().id(UUID.randomUUID()).roomNumber(2).seatNumber(4).dailyCost(100).build();
        Apartment apartment2 = Apartment.builder().id(UUID.randomUUID()).roomNumber(3).seatNumber(6).dailyCost(200).build();
        ApartmentReadDto apartmentDto1 = TestObjectsUtils.getApartmentReadDto(apartment1.getId());
        ApartmentReadDto apartmentDto2 = TestObjectsUtils.getApartmentReadDto(apartment2.getId());
        ApartmentFilter filter = ApartmentFilter.builder().rooms(2).seats(4).build();
        Pageable pageable = PageRequest.of(0, 2);
        Page<Apartment> apartmentPage = new PageImpl<>(List.of(apartment1, apartment2), pageable, 2);
        doReturn(apartmentPage).when(apartmentRepository).findAll(any(Predicate.class), eq(pageable));
        doReturn(apartmentDto1).when(apartmentReadMapper).mapFrom(apartment1);
        doReturn(apartmentDto2).when(apartmentReadMapper).mapFrom(apartment2);

        Page<ApartmentReadDto> result = apartmentService.findAll(filter, pageable);

        assertThat(result.getContent()).containsExactlyInAnyOrder(apartmentDto1, apartmentDto2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getNumber()).isZero();
        assertThat(result.getSize()).isEqualTo(2);
        verify(apartmentRepository).findAll(any(Predicate.class), eq(pageable));
        verify(apartmentReadMapper).mapFrom(apartment1);
        verify(apartmentReadMapper).mapFrom(apartment2);
    }
}
