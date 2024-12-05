package com.bolun.hotel.mapper;

import com.bolun.hotel.dto.UserDetailReadDto;
import com.bolun.hotel.entity.UserDetail;
import com.bolun.hotel.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserDetailReadMapper implements Mapper<UserDetail, UserDetailReadDto> {

    @Override
    public UserDetailReadDto mapFrom(UserDetail userDetail) {
        UUID userId = getUserId();

        return new UserDetailReadDto(
                userDetail.getId(),
                userDetail.getPhoneNumber(),
                userDetail.getPhoto(),
                userDetail.getBirthdate(),
                userDetail.getMoney(),
                userId
        );
    }

    private UUID getUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(CustomUserDetails.class::isInstance)
                .map(principal -> ((CustomUserDetails) principal).getId())
                .orElse(null);
    }
}

