package com.bolun.hotel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "user_detail", schema = "hotel_schema", catalog = "hotel_repository")
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "phone_number", unique = true, nullable = false, length = 64)
    private String phoneNumber;

    @Column(name = "photo", unique = true, length = 128)
    private String photo;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "money", nullable = false)
    private Integer money;

    private UUID userId;
}

