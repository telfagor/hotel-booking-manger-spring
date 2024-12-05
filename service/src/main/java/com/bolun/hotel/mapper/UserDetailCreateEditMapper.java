package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.UserDetailCreateEditDto;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
        userDetail.setPhoneNumber(userDetailDto.phoneNumber());
        userDetail.setBirthdate(userDetailDto.birthdate());
        userDetail.setMoney(userDetailDto.money());
        userDetail.setPhoto(userDetail.getPhoto() != null
                ? userDetail.getPhoto()
                : userDetailDto.photo().getOriginalFilename());

        if (userDetail.getUser() == null) {
            Optional<String> email = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                    .map(authentication -> (UserDetails) authentication.getPrincipal())
                    .map(UserDetails::getUsername);

            email.flatMap(userRepository::findByEmail)
                    .ifPresent(userDetail::add);
        }
    }
}
