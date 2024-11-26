package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.UserDetailCreateEditDto;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDetailCreateEditMapper implements Mapper<UserDetailCreateEditDto, UserDetail> {

    private final UserRepository userRepository;

    @Override
    public UserDetail mapFrom(UserDetailCreateEditDto userDetailDto) {
       UserDetail userDetail = new UserDetail();
       copy(userDetailDto, userDetail);
       return userDetail;
    }

    @Override
    public UserDetail mapFrom(UserDetailCreateEditDto fromUserDetail, UserDetail toUserDetail) {
        copy(fromUserDetail, toUserDetail);
        return toUserDetail;
    }

    private void copy(UserDetailCreateEditDto userDetailDto, UserDetail userDetail) {
        userDetail.setPhoneNumber(userDetail.getPhoneNumber());
        userDetail.setBirthdate(userDetail.getBirthdate());
        userDetail.setMoney(userDetail.getMoney());
        userRepository.findById(userDetailDto.userId()).ifPresent(userDetail::setUser);
    }
}
