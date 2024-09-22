package com.bolun.hotel.entity;

import com.bolun.hotel.entity.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "apartment", schema = "hotel_schema", catalog = "hotel_repository")
public class Apartment {

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
    private Type type;

    @Column(name = "photo", unique = true, nullable = false, length = 128)
    private String photo;
}
