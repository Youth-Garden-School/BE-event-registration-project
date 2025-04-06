package com.eventregistration.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SocialLinks")
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SocialLinks extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "UserId", unique = true, nullable = false)
    User user;

    String instagram;
    String twitter;
    String youtube;
    String linkedin;
    String tiktok;
    String website;
}
