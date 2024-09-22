package com.bolun.hotel.entity;

import com.bolun.hotel.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order", schema = "hotel_schema", catalog = "hotel_repository")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Column(name = "total_cost", nullable = false)
    private Integer totalCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 64)
    private Status status;
}
