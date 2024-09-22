package com.bolun.hotel.entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record Birthdate(LocalDate birthdate) {

    public long getAge() {
        return ChronoUnit.YEARS.between(LocalDate.now(), birthdate);
    }
}
