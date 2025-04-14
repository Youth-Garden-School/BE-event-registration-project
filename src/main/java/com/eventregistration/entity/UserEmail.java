package com.eventregistration.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "UserEmail")
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

    @Builder.Default
    boolean isPrimary = false;

    @Builder.Default
    boolean isVerified = false;
}
