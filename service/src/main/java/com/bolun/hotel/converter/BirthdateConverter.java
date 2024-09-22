package com.bolun.hotel.converter;

import com.bolun.hotel.entity.Birthdate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Date;

import static java.util.Optional.ofNullable;

@Converter(autoApply = true)
public class BirthdateConverter implements AttributeConverter<Birthdate, Date> {

    @Override
    public Date convertToDatabaseColumn(Birthdate birthdate) {
        return ofNullable(birthdate)
                .map(Birthdate::birthdate)
                .map(Date::valueOf)
                .orElse(null);
    }

    @Override
    public Birthdate convertToEntityAttribute(Date date) {
        return ofNullable(date)
                .map(Date::toLocalDate)
                .map(Birthdate::new)
                .orElse(null);
    }
}
