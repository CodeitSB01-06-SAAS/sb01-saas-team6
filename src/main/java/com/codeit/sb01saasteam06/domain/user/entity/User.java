package com.codeit.sb01saasteam06.domain.user.entity;

import com.codeit.sb01saasteam06.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean locked = false;

    @ElementCollection
    @CollectionTable(name = "user_linked_oauth_providers", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "provider")
    private List<String> linkedOAuthProviders;

    public void changeRole(Role role) {
        this.role = role;
    }

    public void changeLocked(boolean locked) {
        this.locked = locked;
    }

    public void changePassword(String password) {
        this.password = password;
    }
}