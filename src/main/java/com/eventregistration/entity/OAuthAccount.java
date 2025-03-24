package com.eventregistration.entity;

import com.eventregistration.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "oauthAccounts")
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OAuthAccount extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    User user;

    String provider;

    @Column(unique = true)
    String providerId;

    String email;
    String name;
    String avatarUrl;
}