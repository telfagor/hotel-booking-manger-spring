package com.bolun.hotel.validation;

import java.time.LocalDate;

public interface DataRangeValidator {

    LocalDate getCheckIn();

    LocalDate getCheckOut();
}
