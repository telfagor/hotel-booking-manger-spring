package com.bolun.hotel.entity;

import com.bolun.hotel.entity.enums.ApartmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(exclude = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "apartment", schema = "hotel_schema", catalog = "hotel_repository")
public class Apartment implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "rooms", nullable = false)
    private Integer roomNumber;

    @Column(name = "seats", nullable = false)
    private Integer seatNumber;

    @Column(name = "daily_cost", nullable = false)
    private Integer dailyCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 28)
    private ApartmentType apartmentType;

    @Column(name = "photo", nullable = false, length = 128)
    private String photo;

    @Builder.Default
    @OneToMany(mappedBy = "apartment")
    private List<Order> orders = new ArrayList<>();

    public void add(Order order) {
        order.setApartment(this);
        this.orders.add(order);
    }
}


