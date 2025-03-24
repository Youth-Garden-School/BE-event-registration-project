package com.eventregistration.entity;

import com.eventregistration.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "userEmails")
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UserEmail extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    User user;

    @Column(unique = true)
    String email;

    boolean isPrimary = false;
    boolean isVerified = false;
}