package com.bolun.hotel.service;

import com.bolun.hotel.dto.ApartmentCreateEditDto;
import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.dto.filters.ApartmentFilter;
import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.mapper.ApartmentCreateEditMapper;
import com.bolun.hotel.mapper.ApartmentReadMapper;
import com.bolun.hotel.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentReadMapper apartmentReadMapper;
    private final ApartmentCreateEditMapper apartmentCreateEditMapper;
    private final ImageService imageService;

    public Page<ApartmentReadDto> findAll(ApartmentFilter filter, Pageable pageable) {
        return apartmentRepository.findAll(filter, pageable)
                .map(apartmentReadMapper::mapFrom);
    }

    public Optional<ApartmentReadDto> findById(UUID id) {
        return apartmentRepository.findActiveById(id)
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
    public Optional<ApartmentReadDto> update(UUID id, ApartmentCreateEditDto apartmentDto) {
        return apartmentRepository.findActiveByIdWithLock(id)
                .map(entity -> {
                    uploadImage(apartmentDto.photo());
                    return apartmentCreateEditMapper.mapFrom(apartmentDto, entity);
                })
                .map(apartmentRepository::saveAndFlush)
                .map(apartmentReadMapper::mapFrom);
    }

    @SneakyThrows
    public void uploadImage(MultipartFile photo) {
        if (!photo.isEmpty()) {
            imageService.upload(photo.getOriginalFilename(), photo.getInputStream());
        }
    }

    public Optional<byte[]> findPhoto(UUID id) {
        return apartmentRepository.findById(id)
                .map(Apartment::getPhoto)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @Transactional
    public boolean delete(UUID id) {
        return apartmentRepository.findActiveByIdWithLock(id)
                .map(apartment -> {
                    apartment.setDeleted(true);
                    apartmentRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
