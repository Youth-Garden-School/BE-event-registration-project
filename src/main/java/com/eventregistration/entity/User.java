package com.eventregistration.entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "NewUser")
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    String firstName;
    String lastName;

    @Column(unique = true)
    String username;

    String avatarUrl;
    String bio;
    String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<UserEmail> emails;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    SocialLinks socialLinks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OAuthAccount> oauthAccounts;

    @Column(columnDefinition = "TEXT") // Sử dụng kiểu TEXT
    String refreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Calendar> calendars;

    @ManyToMany(mappedBy = "users")
    Set<Role> roles;
}
