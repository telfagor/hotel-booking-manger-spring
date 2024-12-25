package com.bolun.hotel.service;

import com.bolun.hotel.dto.UserDetailCreateEditDto;
import com.bolun.hotel.dto.UserDetailReadDto;
import com.bolun.hotel.mapper.UserDetailCreateEditMapper;
import com.bolun.hotel.mapper.UserDetailReadMapper;
import com.bolun.hotel.repository.UserDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;
    private final UserDetailCreateEditMapper userDetailCreateEditMapper;
    private final UserDetailReadMapper userDetailReadMapper;
    private final ImageService imageService;

    @Transactional
    public UserDetailReadDto create(UserDetailCreateEditDto userDetailDto) {
        return Optional.of(userDetailDto)
                .map(dto -> {
                    uploadImage(dto.photo());
                    return userDetailCreateEditMapper.mapFrom(dto);
                })
                .map(userDetailRepository::save)
                .map(userDetailReadMapper::mapFrom)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserDetailReadDto> update(UUID id, UserDetailCreateEditDto userDetailDto) {
        return userDetailRepository.findById(id)
                .map(entity -> {
                    uploadImage(userDetailDto.photo());
                    return userDetailCreateEditMapper.mapFrom(userDetailDto, entity);
                })
                .map(userDetailRepository::saveAndFlush)
                .map(userDetailReadMapper::mapFrom);
    }

    @SneakyThrows
    public void uploadImage(MultipartFile photo) {
        if (!photo.isEmpty()) {
            imageService.upload(photo.getOriginalFilename(), photo.getInputStream());
        }
    }

    public Optional<UserDetailReadDto> findByUserId(UUID userId) {
        return userDetailRepository.findActiveByUserId(userId)
                .map(userDetailReadMapper::mapFrom);
    }

    public Optional<UserDetailReadDto> findByPhoneNumber(String phoneNumber) {
        return userDetailRepository.findByPhoneNumber(phoneNumber)
                .map(userDetailReadMapper::mapFrom);
    }
}
