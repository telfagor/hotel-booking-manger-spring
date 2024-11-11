package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.UserDetailReadDto;
import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final UserDetailReadMapper userDetailReadMapper;

    @Override
    public UserReadDto mapFrom(User user) {
        UserDetailReadDto userDetail = ofNullable(user.getUserDetail())
                .map(userDetailReadMapper::mapFrom)
                .orElse(null);

        return new UserReadDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getGender(),
                user.getRole(),
                userDetail
        );
    }
}
