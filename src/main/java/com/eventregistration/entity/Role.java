package com.eventregistration.entity;

import com.eventregistration.entity.common.BaseAuthor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseAuthor {

    @Column(unique = true, nullable = false)
    String name;

    String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "UserToRole",
            joinColumns = @JoinColumn(name = "RoleId"),
            inverseJoinColumns = @JoinColumn(name = "UserId"))
    Set<User> users;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "RoleToPermission",
            joinColumns = @JoinColumn(name = "RoleId"),
            inverseJoinColumns = @JoinColumn(name = "PermissionId"))
    Set<Permission> permissions;
}