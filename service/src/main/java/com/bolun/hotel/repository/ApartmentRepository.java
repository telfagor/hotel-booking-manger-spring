package com.bolun.hotel.repository;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.entity.Order;
import com.bolun.hotel.util.ApartmentFilter;
import com.bolun.hotel.util.QuerydslPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.bolun.hotel.entity.QApartment.apartment;
import static com.bolun.hotel.entity.QOrder.order;

public class ApartmentRepository extends AbstractRepository<UUID, Apartment> {

    public ApartmentRepository(EntityManager entityManager) {
        super(entityManager, Apartment.class);
    }

    public Map<Apartment, List<LocalDate>> findAll(ApartmentFilter filter) {
        LocalDate checkIn = filter.getCheckIn();
        LocalDate checkOut = filter.getCheckOut();

        Predicate predicate = QuerydslPredicate.builder()
                .add(filter.getRooms(), apartment.roomNumber::eq)
                .add(filter.getSeats(), apartment.seatNumber::eq)
                .add(filter.getDailyCost(), apartment.dailyCost::eq)
                .add(filter.getType(), apartment.apartmentType::eq)
                .build();

        List<Apartment> apartments = new JPAQuery<Apartment>(entityManager)
                .select(apartment)
                .from(apartment)
                .where(predicate)
                .fetch();

        Map<Apartment, List<LocalDate>> availableApartmentsWithDays = new HashMap<>();

        for (Apartment apt : apartments) {
            List<Order> overlappingOrders = new JPAQuery<Apartment>(entityManager)
                    .select(order)
                    .from(order)
                    .where(order.apartment.eq(apt)
                            .and(order.checkIn.loe(checkOut))
                            .and(order.checkOut.goe(checkIn))
                    )
                    .fetch();

            List<LocalDate> availableDays = calculateAvailableDays(checkIn, checkOut, overlappingOrders);

            if (!availableDays.isEmpty()) {
                availableApartmentsWithDays.put(apt, availableDays);
            }
        }

        return availableApartmentsWithDays;
    }

    private List<LocalDate> calculateAvailableDays(LocalDate checkIn, LocalDate checkOut, List<Order> overlappingOrders) {
        List<LocalDate> availableDays = new ArrayList<>();

        LocalDate current = checkIn;
        while (!current.isAfter(checkOut)) {
            boolean isAvailable = true;

            for (Order order : overlappingOrders) {
                if (!current.isBefore(order.getCheckIn()) && !current.isAfter(order.getCheckOut())) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                availableDays.add(current);
            }

            current = current.plusDays(1);
        }

        return availableDays;
    }
}


