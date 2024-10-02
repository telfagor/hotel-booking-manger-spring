package com.bolun.hotel.entity;

import com.bolun.hotel.entity.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Data
@ToString(exclude = {"apartment", "user"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "\"order\"", schema = "hotel_schema", catalog = "hotel_repository")
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
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id", referencedColumnName = "id", nullable = false)
    private Apartment apartment;

    public void add(User user) {
        user.getOrders().add(this);
        this.user = user;
    }

    public void add(Apartment apartment) {
        apartment.getOrders().add(this);
        this.apartment = apartment;
    }
}


