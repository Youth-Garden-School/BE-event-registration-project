package com.eventregistration.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "Permission")
@Getter
@Setter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseAuthor {

    @Column(unique = true, nullable = false)
    String name;

    String description;

    @ManyToMany(
            mappedBy = "permissions",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<Role> roles = new HashSet<>();
}
