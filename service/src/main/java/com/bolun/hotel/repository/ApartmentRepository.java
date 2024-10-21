package com.bolun.hotel.repository;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.util.ApartmentFilter;
import com.bolun.hotel.util.QuerydslPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.bolun.hotel.entity.QApartment.apartment;
import static com.bolun.hotel.entity.QOrder.order;

@Repository
public class ApartmentRepository extends AbstractRepository<UUID, Apartment> {

    @Autowired
    public ApartmentRepository(EntityManager entityManager) {
        super(entityManager, Apartment.class);
    }

    public List<Apartment> findAll(ApartmentFilter filter) {
        LocalDate checkIn = filter.getCheckIn();
        LocalDate checkOut = filter.getCheckOut();

        Predicate predicate = QuerydslPredicate.builder()
                .add(filter.getRooms(), apartment.roomNumber::eq)
                .add(filter.getSeats(), apartment.seatNumber::eq)
                .add(filter.getDailyCost(), apartment.dailyCost::eq)
                .add(filter.getType(), apartment.apartmentType::eq)
                .build();

        BooleanExpression overlappingBooking = order.checkIn.lt(checkOut)
                .and(order.checkOut.gt(checkIn));

        return new JPAQuery<Apartment>(entityManager)
                .select(apartment)
                .from(apartment)
                .leftJoin(apartment.orders, order)
                .on(overlappingBooking)
                .where(predicate)
                .where(order.isNull())
                .fetch();
    }
}


