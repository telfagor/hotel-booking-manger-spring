package com.bolun.hotel.service;

import com.bolun.hotel.dto.ChangePasswordDto;
import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.dto.filters.UserFilter;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.exception.InvalidOldPasswordException;
import com.bolun.hotel.mapper.Mapper;
import com.bolun.hotel.mapper.UserCreateEditMapper;
import com.bolun.hotel.mapper.UserReadMapper;
import com.bolun.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateEditMapper userCreateEditMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    public Page<UserReadDto> findAll(UserFilter filter, Pageable pageable) {
        return userRepository.findAll(filter, pageable)
                .map(userReadMapper::mapFrom);
    }

    public Optional<UserReadDto> findById(UUID id) {
        return userRepository.findById(id)
                .map(userReadMapper::mapFrom);
    }

    public <T> Optional<T> findById(UUID id, Mapper<User, T> mapper) {
        return userRepository.findById(id)
                .map(mapper::mapFrom);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + email));
    }

    public Optional<UserReadDto> findByEmail(String email) {
        Optional<User> maybeUser = userRepository.findByEmail(email);
        return maybeUser.map(userReadMapper::mapFrom);
    }


    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) {
        return Optional.of(userDto)
                .map(userCreateEditMapper::mapFrom)
                .map(userRepository::save)
                .map(userReadMapper::mapFrom)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserReadDto> update(UUID id, UserCreateEditDto userDto) {
        return userRepository.findById(id)
                .map(entity -> {
                    uploadImage(userDto.photo());
                    return userCreateEditMapper.mapFrom(userDto, entity);
                })
                .map(userRepository::saveAndFlush)
                .map(updatedUser -> {
                    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
                    CustomUserDetails updatedDetails = new CustomUserDetails(updatedUser);

                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    updatedDetails,
                                    currentAuth.getCredentials(),
                                    updatedDetails.getAuthorities()
                            )
                    );
                    return userReadMapper.mapFrom(updatedUser);
                });
    }

    @SneakyThrows
    private void uploadImage(MultipartFile photo) {
        if (!photo.isEmpty()) {
            imageService.upload(photo.getOriginalFilename(), photo.getInputStream());
        }
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

    public Optional<byte[]> findAvatar(UUID id) {
        return userRepository.findById(id)
                .map(User::getUserDetail)
                .map(UserDetail::getPhoto)
                .filter(StringUtils::hasText)
                .flatMap(imageService::get);
    }

    @Transactional
    public void changePassword(UUID id, ChangePasswordDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new InvalidOldPasswordException("The current password is incorrect");
        }

        String hashedCurrentPassword = passwordEncoder.encode(dto.currentPassword());

        user.setPassword(hashedCurrentPassword);
        userRepository.save(user);
    }
}
