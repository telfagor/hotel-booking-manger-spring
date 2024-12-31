package com.bolun.hotel.entity;

import com.bolun.hotel.entity.enums.OrderStatus;
import jakarta.persistence.Cacheable;
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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;
import java.util.UUID;

@Data
@ToString(exclude = {"apartment", "user"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "deleted", callSuper = false)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
@Builder
@Entity
@Table(name = "\"order\"", schema = "hotel_schema", catalog = "hotel_repository")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Order extends AuditingEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Column(name = "total_cost", nullable = false)
    private Integer totalCost;

    @Column(name = "deleted", nullable = false, insertable = false)
    private Boolean deleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 64)
    private OrderStatus status;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @NotAudited
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


