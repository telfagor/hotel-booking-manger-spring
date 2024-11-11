package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

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

        if (userDto.password() != null) {
            user.setPassword(userDto.password());
        }
    }
}
