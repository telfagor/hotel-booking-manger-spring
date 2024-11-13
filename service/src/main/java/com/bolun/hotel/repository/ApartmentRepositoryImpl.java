package com.bolun.hotel.repository;

import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.util.ApartmentFilter;
import com.bolun.hotel.util.QuerydslPredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bolun.hotel.entity.QApartment.apartment;
import static com.bolun.hotel.entity.QOrder.order;

@RequiredArgsConstructor
@Repository
public class ApartmentRepositoryImpl implements FilterApartmentRepository {

    private final EntityManager entityManager;

    @Override
    public List<Apartment> findAll(ApartmentFilter filter) {
        Predicate predicate = QuerydslPredicate.builder()
                .add(filter.getRooms(), apartment.roomNumber::eq)
                .add(filter.getSeats(), apartment.seatNumber::eq)
                .add(filter.getDailyCost(), apartment.dailyCost::eq)
                .add(filter.getType(), apartment.apartmentType::eq)
                .buildAnd();

        return new JPAQuery<>(entityManager)
                .select(apartment)
                .from(apartment)
                .leftJoin(apartment.orders, order)
                .on(order.checkIn.lt(filter.getCheckOut())
                        .and(order.checkOut.goe(filter.getCheckIn()))
                )
                .where(predicate, order.id.isNull())
                .fetch();
    }
}




