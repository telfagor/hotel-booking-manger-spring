package com.bolun.hotel.validation;

import com.bolun.hotel.dto.UserCreateEditDto;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Не знаю как использовать этот класс в dto только для CreateAction.
 * В UserCreateEditDto у меня @NotBlank и @Email нужны и для создания и для обновления
 * Этот класс как пример, решил через собственную аннотацию
 */

@RequiredArgsConstructor
@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        if (!errors.getObjectName().endsWith("UpdateAction")) {
            return;
        }

        UserCreateEditDto userDto = (UserCreateEditDto) o;

        if (userService.findByEmail(userDto.email()).isPresent()) {
            errors.rejectValue("email", "", "This email is already taken");
        }
    }
}
