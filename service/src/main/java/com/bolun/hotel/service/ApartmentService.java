package com.bolun.hotel.service;

import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.mapper.ApartmentReadMapper;
import com.bolun.hotel.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;

    private final ApartmentReadMapper apartmentReadMapper;

    public List<ApartmentReadDto> findAll() {
        return apartmentRepository.findAll().stream()
                .map(apartmentReadMapper::mapFrom)
                .toList();
    }
}
