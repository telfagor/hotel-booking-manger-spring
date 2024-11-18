package com.bolun.hotel.service;

import com.bolun.hotel.dto.ApartmentCreateEditDto;
import com.bolun.hotel.dto.ApartmentFilter;
import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.mapper.ApartmentCreateEditMapper;
import com.bolun.hotel.mapper.ApartmentReadMapper;
import com.bolun.hotel.repository.ApartmentRepository;
import com.bolun.hotel.util.QuerydslPredicate;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static com.bolun.hotel.entity.QApartment.apartment;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentReadMapper apartmentReadMapper;
    private final ApartmentCreateEditMapper apartmentCreateEditMapper;

    private final ImageService imageService;

    public Page<ApartmentReadDto> findAll(ApartmentFilter filter, Pageable pageable) {
        Predicate reservationPredicate = ExpressionUtils.anyOf(
                filter.getCheckIn() != null && filter.getCheckOut() != null
                        ? ExpressionUtils.anyOf(
                        apartment.orders.any().checkIn.lt(filter.getCheckOut()),
                        apartment.orders.any().checkOut.goe(filter.getCheckIn())
                )
                        : null
        );

        Predicate predicate = QuerydslPredicate.builder()
                .add(filter.getRooms(), apartment.roomNumber::eq)
                .add(filter.getSeats(), apartment.seatNumber::eq)
                .add(filter.getDailyCost(), apartment.dailyCost::eq)
                .add(filter.getType(), apartment.apartmentType::eq)
                .add(reservationPredicate, Function.identity())
                .buildAnd();

        return apartmentRepository.findAll(predicate, pageable)
                .map(apartmentReadMapper::mapFrom);
    }

    public Optional<ApartmentReadDto> findById(UUID id) {
        return apartmentRepository.findById(id)
                .map(apartmentReadMapper::mapFrom);
    }

    @Transactional
    public ApartmentReadDto create(ApartmentCreateEditDto apartmentDto) {
        return Optional.of(apartmentDto)
                .map(dto -> {
                    uploadImage(dto.photo());
                    return apartmentCreateEditMapper.mapFrom(dto);
                })
                .map(apartmentRepository::save)
                .map(apartmentReadMapper::mapFrom)
                .orElseThrow();
    }

    @Transactional
    public ApartmentReadDto update(UUID id, ApartmentCreateEditDto apartmentDto) {
        return apartmentRepository.findById(id)
                .map(entity -> {
                    uploadImage(apartmentDto.photo());
                    return apartmentCreateEditMapper.mapFrom(apartmentDto, entity);
                })
                .map(apartmentRepository::saveAndFlush)
                .map(apartmentReadMapper::mapFrom)
                .orElseThrow();
    }

    @SneakyThrows
    private void uploadImage(MultipartFile photo) {
        if (!photo.isEmpty()) {
            imageService.upload(photo.getOriginalFilename(), photo.getInputStream());
        }
    }

    @Transactional
    public boolean delete(UUID id) {
        return apartmentRepository.findById(id)
                .map(entity -> {
                    apartmentRepository.delete(entity);
                    apartmentRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
