package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.UserDetailReadDto;
import com.bolun.hotel.entity.UserDetail;
import org.springframework.stereotype.Component;

@Component
public class UserDetailReadMapper implements Mapper<UserDetail, UserDetailReadDto> {

    @Override
    public UserDetailReadDto mapFrom(UserDetail userDetail) {
        return new UserDetailReadDto(
                userDetail.getId(),
                userDetail.getPhoneNumber(),
                userDetail.getPhoto(),
                userDetail.getBirthdate(),
                userDetail.getMoney()
        );
    }
}
