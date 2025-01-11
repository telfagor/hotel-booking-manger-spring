package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.entity.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
        user.setRole(userDto.role());

        if (StringUtils.hasText(userDto.phoneNumber())) {
            UserDetail userDetail = new UserDetail();
            updateUserDetail(userDto, userDetail);
            user.add(userDetail);
        }

        setEncodedPassword(userDto, user);
    }

    private void updateUserDetail(UserCreateEditDto userDto, UserDetail userDetail) {
        userDetail.setPhoneNumber(userDto.phoneNumber());
        userDetail.setBirthdate(userDto.birthdate());
        userDetail.setMoney(userDto.money());

        Optional.ofNullable(userDto.photo())
                .map(MultipartFile::getOriginalFilename)
                .filter(filename -> !filename.isEmpty())
                .ifPresent(userDetail::setPhoto);
    }

    private void setEncodedPassword(UserCreateEditDto userDto, User user) {
        Optional.ofNullable(userDto.rawPassword())
                .filter(StringUtils::hasText)
                .map(passwordEncoder::encode)
                .ifPresent(user::setPassword);
    }
}
