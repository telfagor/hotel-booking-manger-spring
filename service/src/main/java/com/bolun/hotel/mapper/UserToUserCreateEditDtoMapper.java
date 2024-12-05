package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import org.springframework.stereotype.Component;

@Component
public class UserToUserCreateEditDtoMapper implements Mapper<User, UserCreateEditDto> {

    @Override
    public UserCreateEditDto mapFrom(User user) {
        UserDetail userDetail = user.getUserDetail();

        return new UserCreateEditDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                null,
                null,
                user.getGender(),
                userDetail != null ? userDetail.getPhoneNumber() : null,
                userDetail != null ? userDetail.getMoney() : null,
                userDetail != null ? userDetail.getBirthdate() : null,
                null
        );
    }
}
