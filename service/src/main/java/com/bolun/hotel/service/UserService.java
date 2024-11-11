package com.bolun.hotel.service;

import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.mapper.UserCreateEditMapper;
import com.bolun.hotel.mapper.UserReadMapper;
import com.bolun.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;

    public List<UserReadDto> findAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::mapFrom)
                .toList();
    }

    public Optional<UserReadDto> findById(UUID id) {
        return userRepository.findById(id)
                .map(userReadMapper::mapFrom);
    }

    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) {
        return of(userDto)
                .map(userCreateEditMapper::mapFrom)
                .map(userRepository::save)
                .map(userReadMapper::mapFrom)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(UUID id, UserCreateEditDto userDto) {
        return userRepository.findById(id)
                .map(entity -> userCreateEditMapper.mapFrom(userDto, entity))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::mapFrom);
    }

    @Transactional
    public boolean delete(UUID id) {
        return userRepository.findById(id)
                .map(entity -> {
                    userRepository.delete(entity);
                    userRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
