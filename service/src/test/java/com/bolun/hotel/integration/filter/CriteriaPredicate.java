package com.bolun.hotel.integration.filter;

import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CriteriaPredicate {

    @Getter
    private final List<Predicate> predicates = new ArrayList<>();

    public static CriteriaPredicate builder() {
        return new CriteriaPredicate();
    }

    public <T> CriteriaPredicate add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public <T, N> CriteriaPredicate addNested(T object, Function<T, N> nestedFunction, Function<N, Predicate> function) {
        if (object != null) {
            N nestedObject = nestedFunction.apply(object);
            if (nestedObject != null) {
                predicates.add(function.apply(nestedObject));
            }
        }
        return this;
    }

    public Predicate build() {
        return predicates.isEmpty() ? null : predicates.get(0);
    }
}
