package com.bolun.hotel.validation;

import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.dto.UserDetailReadDto;
import com.bolun.hotel.dto.UserReadDto;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.service.UserDetailService;
import com.bolun.hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserValidator implements Validator {

    private final UserService userService;
    private final UserDetailService userDetailService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserCreateEditDto userDto = (UserCreateEditDto) o;

        Optional<UserReadDto> userReadDto = userService.findByEmail(userDto.email());
        boolean isPhoneNumberIsPresent = userDetailService.findByPhoneNumber(userDto.phoneNumber()).isPresent();

        if (userReadDto.isPresent() && userReadDto.get().userDetail() != null) {
            UserReadDto user = userReadDto.get();
            UserDetailReadDto userDetail = user.userDetail();
            if (user.email().equals(userDto.email()) && userDetail.phoneNumber().equals(userDto.phoneNumber())) {
                return;
            } else if (isPhoneNumberIsPresent) {
                errors.rejectValue("phoneNumber", "", "This phone is already taken");
                return;
            }
        }

        if (StringUtils.hasText(userDto.phoneNumber()) && isPhoneNumberIsPresent) {
                errors.rejectValue("phoneNumber", "", "This phone is already taken");
        }
    }
}
