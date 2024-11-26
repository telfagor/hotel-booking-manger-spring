package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User mapFrom(UserCreateEditDto userDto, User user) {
        copy(userDto, user);
        return user;
    }

    @Override
    public User mapFrom(UserCreateEditDto userDto) {
        User user = new User();
        copy(userDto, user);
        return user;
    }

    private void copy(UserCreateEditDto userDto, User user) {
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setEmail(userDto.email());
        user.setGender(userDto.gender());

        Optional.ofNullable(userDto.rawPassword())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(user::setPassword);
    }
}
