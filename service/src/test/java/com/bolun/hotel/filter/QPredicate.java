package com.bolun.hotel.filter;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class QPredicate {

    @Getter
    private List<Predicate> predicates = new ArrayList<>();

    @Getter
    private List<BooleanExpression> querydslPredicates = new ArrayList<>();

    public static QPredicate builder() {
        return new QPredicate();
    }

    public <T> QPredicate add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }

        return this;
    }

    public <T, N> QPredicate addNested(T object, Function<T, N> nestedFunction, Function<N, Predicate> function) {
        if (object != null) {
            N nestedObject = nestedFunction.apply(object);
            if (nestedObject != null) {
                predicates.add(function.apply(nestedObject));
            }
        }

        return this;
    }

    public <T> QPredicate addQuerydslPredicate(T object, Function<T, BooleanExpression> function) {
        if (object != null) {
            querydslPredicates.add(function.apply(object));
        }
        return this;
    }

    public Predicate buildCriteria() {
        return predicates.isEmpty() ? null : predicates.get(0);
    }

    public BooleanExpression buildQuerydsl() {
        return querydslPredicates.stream()
                .reduce(BooleanExpression::and)
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }
}
