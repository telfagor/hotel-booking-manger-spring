package com.bolun.hotel.repository;

import com.bolun.hotel.dto.filters.UserFilter;
import com.bolun.hotel.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FilterUserRepository {

    Page<User> findAll(UserFilter filter, Pageable pageable);
}
