package com.bolun.hotel.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@ToString(exclude = "user")
@EqualsAndHashCode(of = "phoneNumber", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
@Builder
@Entity
@Table(name = "user_detail", schema = "hotel_schema", catalog = "hotel_repository")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class UserDetail extends AuditingEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "phone_number", unique = true, nullable = false, length = 64)
    private String phoneNumber;

    @Column(name = "photo", length = 128)
    private String photo;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "money", nullable = false)
    private Integer money;

    @NotAudited
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    public void add(User user) {
        user.setUserDetail(this);
        this.user = user;
    }
}


