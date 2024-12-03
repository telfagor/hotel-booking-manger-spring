package com.bolun.hotel.repository;

import com.bolun.hotel.dto.filters.UserFilter;
import com.bolun.hotel.entity.User;
import com.bolun.hotel.util.QuerydslPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.bolun.hotel.entity.QUser.user;
import static com.bolun.hotel.entity.QUserDetail.userDetail;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;

    @Override
    public Page<User> findAll(UserFilter filter, Pageable pageable) {
        Predicate predicate = getPredicate(filter);
        List<User> users = new JPAQuery<>(entityManager)
                .select(user)
                .from(user)
                .leftJoin(user.userDetail).fetchJoin()
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalElements = getTotalElements(predicate);

        return new PageImpl<>(users, pageable, totalElements);
    }

    private Predicate getPredicate(UserFilter filter) {
        return QuerydslPredicate.builder()
                .add(filter.email(), user.email::containsIgnoreCase)
                .add(filter.firstName(), user.firstName::containsIgnoreCase)
                .add(filter.lastName(), user.lastName::containsIgnoreCase)
                .add(filter.gender(), user.gender::eq)
                .add(filter.role(), user.role::eq)
                .add(filter.phoneNumber(), user.userDetail.phoneNumber::containsIgnoreCase)
                .add(filter.birthdateFrom(), user.userDetail.birthdate::goe)
                .add(filter.birthdateTo(), user.userDetail.birthdate::loe)
                .add(filter.money(), user.userDetail.money::eq)
                .buildAnd();
    }

    private Long getTotalElements(Predicate predicate) {
        return new JPAQuery<>(entityManager)
                .select(user.count())
                .from(user)
                .join(user.userDetail, userDetail)
                .where(predicate)
                .fetchOne();
    }
}
